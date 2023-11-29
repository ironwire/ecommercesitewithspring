package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class UserRepositoryTest {
	
	@Autowired
	private UserRepository urepo;
	
	@Autowired
	private RoleRepository rrepo;
	
	@Autowired
	private TestEntityManager entityManager;
	
	@Test
	public void testCreateUser() {
		Role newBie1R = new Role("admin", "do everything together");
		rrepo.save(newBie1R);
		
		User newbie1 = new User("lixd@sina.com", "password", "John", "Smith");

		newbie1.addRole(newBie1R);
		urepo.save(newbie1);
	}
	
	@Test
	public void testCreateUserWithRole(){
		Role roleAdmin = entityManager.find(Role.class, 2);
		
		User newbie2= new User("ironwire@sina.com", "hahahah", "Jack", "Bond");
		
		newbie2.addRole(roleAdmin);
		
		User savedNew = urepo.save(newbie2);
		
		assertThat(savedNew.getId()).isGreaterThan(0);
		
	}
	
	@Test
	public void testListAllUsers() {
		
		Iterable<User> listUsers = urepo.findAll();
		listUsers.forEach(user -> System.out.println(user));
	}
	
}

