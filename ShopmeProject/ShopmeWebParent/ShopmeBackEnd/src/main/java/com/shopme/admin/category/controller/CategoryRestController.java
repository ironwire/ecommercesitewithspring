package com.shopme.admin.category.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.category.service.CategoryService;

@RestController
public class CategoryRestController {

	@Autowired
	private CategoryService service;
	
	@PostMapping("/categories/check_unique")
	public String checkUnique(@Param("id") Integer id,
			@Param("name") String name,
			@Param("alias") String alias) {
		return service.checkUnique(id, name, alias);
	}
	
	@PostMapping("/categories/check_children")
	public String checkIsExistingChildren(@Param("id") Integer id) {
		Long count = service.countByParentId(id);
		
		System.out.println("count is llllll "+count+" "+id);
		
		String responseString = "No";
		if(count == 0) {
			return responseString;
		}else {
			System.out.println("return Yes");
			responseString = "Yes";
			return responseString;
		}
	}
}
