package org.yiqixue.admin.service.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.yiqixue.admin.persistence.user.RoleRepository;
import org.yiqixue.common.user.Role;

@Service
public class RoleService {
	
	@Autowired
	private RoleRepository roleRepo;
	
	public Role createNewRole(Role role) {
		return roleRepo.save(role);
	}
}
