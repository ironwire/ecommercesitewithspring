package com.shopme.admin.category.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.category.service.CategoryService;
import com.shopme.admin.utils.FileUploadUtil;
import com.shopme.common.entity.Category;

@Controller
public class CategoryController {
	
	@Autowired
	CategoryService cservice;
	
	@GetMapping("/categories")
	public String listAll(Model model) {
		List<Category> listCategories = cservice.listAllCategories();
		model.addAttribute("categories", listCategories);
		
		return "categories/categories";
	}
	
	@GetMapping("/categories/new")
	public String newCategory(Model model) {
		
		List<Category> listCategories = cservice.listCategoriesUsedInForm();
		model.addAttribute("pageTitle", "Create a new category");
		model.addAttribute("category", new Category());
		model.addAttribute("listCategories", listCategories);
		return "categories/category_form";
	}
	
	@PostMapping("/categories/save")
	public String saveCategory(Category category,
			@RequestParam("fileImage") MultipartFile multipartFile,RedirectAttributes redAtt) throws IOException{
		
		String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
		category.setImage(fileName);
		
		
		Category savedCategory = cservice.save(category);
		String uploadDir = "../category-images/" + savedCategory.getId();
		FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
		redAtt.addFlashAttribute("message", "Category has been added in sucessfully.");
		return "redirect:/categories";
	}
	
	@GetMapping("/categories/edit/{id}")
	public String editCategory(@PathVariable(name="id") Integer id,
			Model model,
			RedirectAttributes redirectAttributes) {
		
		try {
			Category cate  =cservice.getCategoryById(id);
			List<Category> listCategories = cservice.listCategoriesUsedInForm();
			
			model.addAttribute("category",cate);
			model.addAttribute("listCategories", listCategories);
		
			model.addAttribute("pageTitle", "Update Category (ID"+id+")");
		
			return "categories/category_form";
			
		}catch(CategoryNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
			return "redirect:/users";
		}
	}
}
