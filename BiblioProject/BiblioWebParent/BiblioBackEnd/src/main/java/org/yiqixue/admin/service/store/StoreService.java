package org.yiqixue.admin.service.store;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yiqixue.admin.persistence.store.StoreRepository;
import org.yiqixue.admin.persistence.user.RoleRepository;
import org.yiqixue.admin.persistence.user.UserRepository;
import org.yiqixue.admin.service.user.UserService;
import org.yiqixue.admin.utils.EmailUtility;
import org.yiqixue.common.EmailSetting;
import org.yiqixue.common.store.Store;
import org.yiqixue.common.user.Role;
import org.yiqixue.common.user.User;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import jakarta.transaction.Transactional;
import net.bytebuddy.utility.RandomString;

@Service
@Transactional
public class StoreService {
	
	@Autowired
	private StoreRepository storeRepo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	@Autowired
	private EmailSetting setting;
	
//	@Autowired
//	private UserService userService;
	
	@Autowired
	private RoleRepository roleRepo;
	
	@Autowired
	private UserRepository userRepo;
	
	public List<Store> listAll(){
		return storeRepo.findAll();
	}
	
	public Store get(Integer id) {
		return storeRepo.findById(id).get();
	}
	
	public boolean isEmailUnique(String email) {
		Store seller = storeRepo.findByEmail(email);
		return seller == null;
	}
	
	public Store registerSeller(Store store) {

		encodePassword(store);
//		store.setEnabled(false);
//		store.setEmailConfirmed(false);
		store.setCreatedTime(new Date());
		
		String randomCode = RandomString.make(64);
		store.setVerificationCode(randomCode);
		
		store = storeRepo.save(store);
		
		try {
			sendVerificationEmail(store);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return store;
	}
	
	private void encodePassword(Store store) {
		String encodedPassword = passwordEncoder.encode(store.getPassword());
		store.setPassword(encodedPassword);
	}
	
	private void sendVerificationEmail(Store seller) 
			throws UnsupportedEncodingException, MessagingException {

		JavaMailSenderImpl mailSender = EmailUtility.prepareMailSender(setting);
		
		String toAddress = seller.getEmail();
		String subject = setting.getEmailSubject();
		String content = setting.getEmailContent();
		
		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);
		
		helper.setFrom(setting.getFromEmail(), setting.getSenderName());
		helper.setTo(toAddress);
		helper.setSubject(subject);
		
		content = content.replace("[[name]]", seller.getName());
		
		String verifyURL = "http://localhost:4200/seller" + "/verify?code=" + seller.getVerificationCode();
		
		content = content.replace("[[URL]]", verifyURL);
		
		helper.setText(content, true);
		
		mailSender.send(message);
		
		System.out.println("to Address: " + toAddress);
		System.out.println("Verify URL: " + verifyURL);
	}	
	
	public boolean verify(String verificationCode) {
		Store store = storeRepo.findByVerificationCode(verificationCode);
		
		if (store == null || store.isEnabled()) {
			return false;
		} else {
			storeRepo.emailConfirm(store.getId());
			
			Role role = roleRepo.findByName("Seller").get();
			Set<Role> sellerRoles = new HashSet<>();	
			sellerRoles.add(role);
			
			User sellerUser = new User(store.getEmail(), store.getPassword(), store.getFirstName(), store.getLastName(), true);
			sellerUser.setRoles(sellerRoles);
			userRepo.save(sellerUser);
			
			return true;
		}
	
	}

}
