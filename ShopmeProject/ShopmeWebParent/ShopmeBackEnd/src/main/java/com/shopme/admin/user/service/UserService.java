package com.shopme.admin.user.service;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.admin.user.RoleRepository;
import com.shopme.admin.user.UserRepository;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class UserService {

	public static final int USERS_PER_PAGE = 4;
	
	@Autowired
	private UserRepository urepo;
	
	@Autowired
	private RoleRepository rrepo;
	
	@Autowired
	private PasswordEncoder pEncoder;
	
	public List<User> findAllUsers(){
		return (List<User>)urepo.findAll();	
	}
	
	public List<Role> listRoles(){
		return (List<Role>)rrepo.findAll();
	}
	
	
	public Page<User> listByPage(int pageNumber, String sortField, String sortDir, String keyword){
		Sort sort= Sort.by(sortField);
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNumber-1, USERS_PER_PAGE, sort);
		
		if(keyword !=null) {
			return urepo.findAll(keyword, pageable);
		}
		
		return urepo.findAll(pageable);
	}
	
	public User save(User user) {
		
		boolean isUpdatingUser = (user.getId() != null);
		
		if(isUpdatingUser) {
			 User existingUser = urepo.findById(user.getId()).get();
			
			if(user.getPassword().isEmpty()) {
				user.setPassword(existingUser.getPassword());
			}else {
				encodePassword(user);
			}
		} else {
			encodePassword(user);
		}
		
		
		//encodePassword(user);
		return urepo.save(user);
	}
	
	private void encodePassword(User user) {
		String encoded = pEncoder.encode(user.getPassword());
		user.setPassword(encoded);
	}
	
	public boolean isEmailUnique(Integer id, String email) {
		User userByEmail = urepo.getUserByEmail(email);
		
		if(userByEmail == null ) return true;
		
		boolean isCreatingNew = (id==null);
		if(isCreatingNew) {
			if(userByEmail != null) return false;
		} else {
			if(userByEmail.getId() != id) {
				return false;
			}
		}
		
		return true;
	}

	public User getUserById(Integer id) throws UserNotFoundException {
		
		try {
		return urepo.findById(id).get();
		} catch(NoSuchElementException ex) {
			throw new UserNotFoundException("could not find any user with ID "+ id);
		}
	}
	
	
	public void deleteUserById(Integer id) throws UserNotFoundException {
		
		Long countById = urepo.countById(id);
		
		if(countById == null || countById == 0) {
			throw new UserNotFoundException("Could not find any user with ID " + id);
			
		}
		urepo.deleteById(id);
	}
	
	public void updateUserEnabledStatus(Integer id, boolean enabled) {
		urepo.updateEnabledStatus(id, enabled);
	}

	public User getByEmail(String email) {
		User user = urepo.getUserByEmail(email);
		return user;
	}
	
	public User updateAccount(User userInForm) {
		
		System.out.println("password in form"+userInForm.getPassword());
		User userInDb = urepo.findById(userInForm.getId()).get();
		
		if(!userInForm.getPassword().isEmpty()) {
			userInDb.setPassword(userInForm.getPassword());
			encodePassword(userInDb);
		}
		
		if(userInForm.getPhotos()!=null) {
			userInDb.setPhotos(userInForm.getPhotos());
		}
		
		userInDb.setFirstName(userInForm.getFirstName());
		userInDb.setLastName(userInForm.getLastName());
		
		return urepo.save(userInDb);
	}
}















