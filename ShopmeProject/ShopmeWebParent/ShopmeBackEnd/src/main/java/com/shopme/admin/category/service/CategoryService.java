package com.shopme.admin.category.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.admin.category.CategoryRepository;
import com.shopme.admin.category.controller.CategoryNotFoundException;
import com.shopme.common.entity.Category;

@Service
public class CategoryService {
	
	@Autowired
	CategoryRepository crepo;
	
	
	public Category save(Category category) {
		return crepo.save(category);
	}
	
	public List<Category> listAllCategories(){
	
		List<Category> rootCategories=  crepo.findRootCategories();
		return listHierarchicalCategories(rootCategories);
	} 
	
	private List<Category> listHierarchicalCategories(List<Category> rootCategories){
		List<Category> hierarchicalCategories = new ArrayList<>();
		
		for(Category rootcate : rootCategories) {
			hierarchicalCategories.add(Category.copyFull(rootcate));
			
			Set<Category> children = rootcate.getChildren();
			for (Category child : children) {
				String name = "--" + child.getName();
				hierarchicalCategories.add(Category.copyFull(child, name));
			}
			listSubHierarchicalCategories(hierarchicalCategories, rootcate, 1);
		}
		return hierarchicalCategories;
	}
	
	private void listSubHierarchicalCategories(List<Category> hierarchicalCategories, 
			Category parent, int subLevel) {
		
		Set<Category> children = parent.getChildren();
		int newSubLevel = subLevel + 1;
		for (Category sub : children) {
			String name ="";
			for(int i=0; i < newSubLevel; i++) {
				name += "--";
			}
			name += sub.getName();
			hierarchicalCategories.add(Category.copyFull(sub, name));
			
			listSubHierarchicalCategories(hierarchicalCategories, sub, newSubLevel);
		}
	}
	
	public List<Category> listCategoriesUsedInForm(){
		List<Category> categoriesUsedInForm = new ArrayList<>();
		
		Iterable<Category> categoriesInDB = crepo.findAll();
		
		for (Category category : categoriesInDB) {
			if(category.getParent() == null) {
				categoriesUsedInForm.add(Category.copyIdAndName(category));
				
				Set<Category> children =category.getChildren();
				for(Category subCategory : children) {
					String name ="--" + subCategory.getName();
					categoriesUsedInForm.add(Category.copyIdAndName(subCategory));
					
					listChildren(categoriesUsedInForm,subCategory,1);
				}
			}
		}
		
		
		
		return categoriesUsedInForm;
	}

	private void listChildren(List<Category> categoriesUsedInForm,
			Category parent, int subLevel) {
		int newSubLevel = subLevel + 1;
		Set<Category> children = parent.getChildren();
		for(Category subCategory : children) {
			String name = "";
			for(int i= 0; i<newSubLevel; i++) {
				name += "--";
			}
			name += subCategory.getName();
			categoriesUsedInForm.add(new Category(name));
			listChildren(categoriesUsedInForm, subCategory, newSubLevel);
		}
		
	}
	
	public Category getCategoryById(Integer id) throws CategoryNotFoundException{
		return crepo.findById(id).get();
	}
	
	public String checkUnique(Integer id, String name, String alias) {
		
		boolean isCreatingNew = (id == null || id == 0);
		
		Category categoryByName = crepo.findByName(name);
		
		if(isCreatingNew) {
			if(categoryByName != null) {
				return "DuplicatedName";
			}else {
				Category categoryByAlias = crepo.findByAlias(alias);
				if(categoryByAlias != null) {
					return "DuplicatedAlias";
				}
			}
		}
		return "OK";
		
	}
}
