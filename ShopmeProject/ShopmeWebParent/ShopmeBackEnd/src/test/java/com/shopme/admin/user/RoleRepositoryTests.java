package com.shopme.admin.user;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.shopme.common.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests {
	
	@Autowired
	private RoleRepository repo;
	
	@Test
	public void testCreateFirstRole() {
		Role roleAdmin = new Role("Admin", "manage everything");
		Role savedRole = repo.save(roleAdmin);
		assertThat(savedRole.getId()).isGreaterThan(0);
	}
	
	@Test
	public void testAddRestRolesToDb() 
	{
		Role roleSales = new Role("Salesperson", "Sell Comb to monk");
		Role savedRole = repo.save(roleSales);
		Role roleEdit = new Role("Editor", "Each typo is a gold worth to dig");
		Role savedR = repo.save(roleEdit);
		Role roleShip = new Role("Shipper", "Shipping missle to the moon");
		Role savedRo = repo.save(roleShip);
		Role roleAss = new Role("Assistant", "Ass is tant? what do you mean?");
		Role savedRol = repo.save(roleAss);
	
		assertThat(savedRole.getId()).isGreaterThan(0);
	}

}
