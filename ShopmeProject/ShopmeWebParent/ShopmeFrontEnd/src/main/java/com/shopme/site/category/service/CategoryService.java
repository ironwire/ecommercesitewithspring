package com.shopme.site.category.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.Category;
import com.shopme.common.exception.CategoryNotFoundException;
import com.shopme.site.category.persistence.CategoryRepository;

@Service
public class CategoryService {

	@Autowired 
	private CategoryRepository crepo;
	
	public List<Category> listNoChildrenCategories(){
		List<Category> listNoChildrenCategories = new ArrayList<>();
		List<Category> listEnabledCategories = crepo.findAllEnabled();
		
		listEnabledCategories.forEach(category ->{
			Set<Category> children = category.getChildren();
			if(children == null || children.size()== 0) {
				listNoChildrenCategories.add(category);
			}
		});
		
		return listNoChildrenCategories;
	}
	
	public Category getCategory(String alias) throws CategoryNotFoundException {
		Category category = crepo.findByAliasEnabled(alias);
		
		if(category == null) {
			throw new CategoryNotFoundException("Could not find any categories with alias: "+alias);
		}
		return crepo.findByAliasEnabled(alias);
	}
	
	public List<Category> getCategoryParents(Category child){
		
		List<Category> listParents = new ArrayList<>();
		
		Category parent = child.getParent();
		
		while(parent != null) {
			listParents.add(0, parent);
			parent = parent.getParent();
		}
		
		listParents.add(child);
		
		return listParents;
	}
}
















