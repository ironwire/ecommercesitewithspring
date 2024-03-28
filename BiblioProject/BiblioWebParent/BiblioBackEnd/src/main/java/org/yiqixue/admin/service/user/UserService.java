package org.yiqixue.admin.service.user;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yiqixue.admin.persistence.user.RoleRepository;
import org.yiqixue.admin.persistence.user.UserRepository;
import org.yiqixue.common.user.Role;
import org.yiqixue.common.user.User;

@Service
public class UserService {

	@Autowired
	private RoleRepository roleRepo;
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private PasswordEncoder encoder;
	
	public Role createNewRoe(Role role) {
		return roleRepo.save(role);
	}
	
	public User registerNewUser(User user) {
		Role role = roleRepo.findByName("BookAdmin").get();
		Set<Role> userRoles = new HashSet<>();	
		userRoles.add(role);
		user.setRoles(userRoles);
		user.setPassword(getEncodedPassword(user.getPassword()));
		
		return userRepo.save(user);
	}
	
	public User registerNewUser(User user, String roleName) {
		Role role = roleRepo.findByName(roleName).get();
		Set<Role> userRoles = new HashSet<>();	
		userRoles.add(role);
		user.setRoles(userRoles);
		user.setPassword(getEncodedPassword(user.getPassword()));
		
		return userRepo.save(user);
	}
    
	public String getEncodedPassword(String password) {
        return encoder.encode(password);
    }
	
	
    public void initRoleAndUser() {

        Role adminRole = new Role();
        adminRole.setName("Admin");  
        adminRole.setDescription("Admin role");
        roleRepo.save(adminRole);

        Role userRole = new Role();
        userRole.setName("User");
        userRole.setDescription("Default role for newly created record");
        roleRepo.save(userRole);

        User adminUser = new User();
        adminUser.setEmail("admin123@qq.com");
        adminUser.setPassword(getEncodedPassword("admin@pass"));
        adminUser.setFirstName("admin");
        adminUser.setLastName("admin");
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(adminRole);
        adminUser.setRoles(adminRoles); 
        userRepo.save(adminUser);

//        User user = new User();
//        user.setUserName("raj123");
//        user.setUserPassword(getEncodedPassword("raj@123"));
//        user.setUserFirstName("raj");
//        user.setUserLastName("sharma");
//        Set<Role> userRoles = new HashSet<>();
//        userRoles.add(userRole);
//        user.setRole(userRoles);
//        userDao.save(user);
    }
}
