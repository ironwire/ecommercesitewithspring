package com.shopme.admin.brand.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shopme.admin.brand.service.BrandNotFoundException;
import com.shopme.admin.brand.service.BrandService;
import com.shopme.admin.dto.CategoryDTO;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;

@RestController
public class BrandRestController {
	
	@Autowired
	private BrandService bservice;
	
	@PostMapping("/brands/check_unique")
	public String checkUnique(@Param("id") Integer id, @Param("name") String name) {
		return bservice.checkUnique(id,name);
	}

	@GetMapping("/brands/{id}/categories")
	public List<CategoryDTO> listCategoriesByBrand(@PathVariable(name = "id") Integer brandId) throws BrandNotFoundException{
		
		List<CategoryDTO> listCategories = new ArrayList<>();
		
		Brand brand;
		try {
			brand = bservice.get(brandId);
			Set<Category> categories = brand.getCategories();
			
			for(Category category: categories) {
				CategoryDTO dto = new CategoryDTO(category.getId(), category.getName());
				listCategories.add(dto);
			}
			
			return listCategories;
			
		} catch (BrandNotFoundException e) {
			throw new BrandNotFoundException("");
		}
	
	}
	
}
