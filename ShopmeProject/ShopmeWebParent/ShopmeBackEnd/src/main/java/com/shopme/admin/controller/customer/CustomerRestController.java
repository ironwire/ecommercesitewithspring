package com.shopme.admin.controller.customer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.service.customer.CustomerService;

@RestController
public class CustomerRestController {
	@Autowired CustomerService cservice;
	
	@PostMapping("/customers/check_email")
	public String checkEmailUniqueness(@RequestParam("email") String email) {
		if(cservice.isEmailUnique(email)) {
			return "OK";
		}else {
			return "Duplicatged";
		}
	} 
}
