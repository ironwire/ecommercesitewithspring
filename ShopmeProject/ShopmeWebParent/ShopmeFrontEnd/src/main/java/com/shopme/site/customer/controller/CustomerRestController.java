package com.shopme.site.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.site.customer.service.CustomerService;

@RestController
public class CustomerRestController {

	@Autowired private CustomerService service;
	
	@PostMapping("/customers/check_unique_email")
	public String checkDuplicateEmail(String email) {
		return service.isEmailUnique(email) ? "OK" : "Duplicated";
	}
}
