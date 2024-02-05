package com.shopme.admin.category.service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.category.CategoryPageInfor;
import com.shopme.admin.category.CategoryRepository;
import com.shopme.common.entity.Category;
import com.shopme.common.exception.CategoryNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CategoryService {
	
	public static final int ROOT_CATEGORIES_PER_PAGE = 4;
	
	@Autowired
	CategoryRepository crepo;
	
	
	public Category save(Category category) {
		
		Category parent = category.getParent();
		if(parent != null) {
			String allParentIds = parent.getAllParentIDs() == null ? "-" : parent.getAllParentIDs();
			allParentIds += String.valueOf(parent.getId())+"-";
			category.setAllParentIDs(allParentIds);
		}
		
		return crepo.save(category);
	}
	
	public List<Category> listAllCategoriesByPage(CategoryPageInfor pageInfo,
			int pageNum, 
			String sortDir, String keyword){
		
		Sort sort= Sort.by("name");
		
		if(sortDir == null || sortDir.isEmpty()) {
			sort = sort.ascending();
		}else if(sortDir.equals("asc")) {
			sort = sort.ascending();
		}else if (sortDir.equals("desc")) {
			sort = sort.descending();
		}
		
		Pageable pageable = PageRequest.of(pageNum-1, ROOT_CATEGORIES_PER_PAGE, sort);
		
		Page<Category> pageCategories = null;
		if(keyword != null && !keyword.isEmpty()) {
			pageCategories=  crepo.search(keyword, pageable);
		}else {
			pageCategories=  crepo.findRootCategories(pageable);	
		}
		
		pageInfo.setTotalElements(pageCategories.getTotalElements());
		pageInfo.setTotalPages(pageCategories.getTotalPages());
		
		if(keyword != null && !keyword.isEmpty()) {
			List<Category> searchResult = pageCategories.getContent();
			return searchResult;
		} else {
			return listHierarchicalCategories(pageCategories.getContent(),sortDir);
		}
		
	} 
	
	private List<Category> listHierarchicalCategories(List<Category> rootCategories, String sortDir){
		List<Category> hierarchicalCategories = new ArrayList<>();
		
		for(Category rootcate : rootCategories) {
			hierarchicalCategories.add(Category.copyFull(rootcate));
			
			Set<Category> children = sortSubCategories(rootcate.getChildren(), sortDir) ;
			for (Category child : children) {
				String name = "--" + child.getName();
				hierarchicalCategories.add(Category.copyFull(child, name));
			}
			listSubHierarchicalCategories(hierarchicalCategories, rootcate, 1,sortDir);
		}
		return hierarchicalCategories;
	}
	
	private void listSubHierarchicalCategories(List<Category> hierarchicalCategories, 
			Category parent, int subLevel, String sortDir) {
		
		Set<Category> children = sortSubCategories( parent.getChildren(), sortDir);
		int newSubLevel = subLevel + 1;
		for (Category sub : children) {
			String name ="";
			for(int i=0; i < newSubLevel; i++) {
				name += "--";
			}
			name += sub.getName();
			hierarchicalCategories.add(Category.copyFull(sub, name));
			
			listSubHierarchicalCategories(hierarchicalCategories, sub, newSubLevel, sortDir);
		}
	}
	
	public List<Category> listCategoriesUsedInForm(){
		List<Category> categoriesUsedInForm = new ArrayList<>();
		
		Iterable<Category> categoriesInDB = crepo.findRootCategories(Sort.by("name").ascending());//findAll();
		
		for (Category category : categoriesInDB) {
			if(category.getParent() == null) {
				categoriesUsedInForm.add(Category.copyIdAndName(category));
				
				Set<Category> children =sortSubCategories(category.getChildren());
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
		Set<Category> children = sortSubCategories(parent.getChildren()) ;
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
	
	private SortedSet<Category> sortSubCategories(Set<Category> children){
		return sortSubCategories(children, "asc");
	}
	
	private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir){
		
		SortedSet<Category> sortedChildren = new TreeSet<>(new Comparator<Category>() {

			@Override
			public int compare(Category o1, Category o2) {
				if (sortDir.equals("asc")) {
					return o1.getName().compareTo(o2.getName());
				}else {
					return o2.getName().compareTo(o1.getName());
				}
			}
			
		});
		sortedChildren.addAll(children);
		return sortedChildren;
	}

	public void updateUserEnabledStatus(Integer id, boolean enabled) {
		crepo.updateEnabledStatus(id, enabled);
		
	}
	
	public Long countByParentId(Integer id) {
		
		return crepo.countByParentId(id);
	}
	
	public void deleteById(Integer id) throws CategoryNotFoundException {
		Long countById = crepo.countById(id);
		
		if(countById == null || countById == 0) {
			throw new CategoryNotFoundException("Could not find any user with ID " + id);
			
		}
		crepo.deleteById(id);
	}
}
