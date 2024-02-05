package org.yiqixue.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;


@SpringBootApplication
@EntityScan({"org.yiqixue.common"})
public class BiblioBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(BiblioBackEndApplication.class, args);
	}

}
