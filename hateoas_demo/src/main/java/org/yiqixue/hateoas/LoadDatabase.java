package org.yiqixue.hateoas;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yiqixue.hateoas.entity.Account;

@Configuration
public class LoadDatabase {

	@Autowired
	private AccountRepository accountRepository;
	
	@Bean
	public CommandLineRunner initDatabase() {
		return Args -> {
	           Account account1 = new Account("1982080185", 1021.99f);
	           Account account2 = new Account("1982032177", 231.50f);
	           Account account3 = new Account("1982094128", 6211.00f);
	             
	           accountRepository.saveAll(List.of(account1, account2, account3));
	             
	           System.out.println("Sample database initialized.");
		};
	}
	
}
