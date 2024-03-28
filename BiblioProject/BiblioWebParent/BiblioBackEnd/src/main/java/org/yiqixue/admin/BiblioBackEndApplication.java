package org.yiqixue.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@EntityScan({"org.yiqixue.common","org.yiqixue.admin"})
@ComponentScan({"org.yiqixue.common","org.yiqixue.admin"})
public class BiblioBackEndApplication {

	public static void main(String[] args) {
		SpringApplication.run(BiblioBackEndApplication.class, args);
	}

}
