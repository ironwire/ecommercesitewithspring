package org.yiqixue.admin.controller.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.yiqixue.admin.service.user.RoleService;
import org.yiqixue.admin.service.user.UserService;
import org.yiqixue.common.user.Role;
import org.yiqixue.common.user.User;
import jakarta.annotation.PostConstruct;

//@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:4200")
@RestController
public class UserRestController {

	@Autowired
	private UserService userService;
    @Autowired
    private RoleService roleService;

    @PostMapping({"/createNewRole"})
    public Role createNewRole(@RequestBody Role role) {
        return roleService.createNewRole(role);
    }
	//@PostConstruct
	public void initRoleAndUser() {
		userService.initRoleAndUser();
	}

	@PostMapping("/registerNewUser")
	public User registerNewUser(@RequestBody User user) {
		return userService.registerNewUser(user);
	}
}
