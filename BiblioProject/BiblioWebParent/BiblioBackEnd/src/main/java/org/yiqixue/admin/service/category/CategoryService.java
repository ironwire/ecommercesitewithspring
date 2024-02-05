package org.yiqixue.admin.service.category;

import java.util.List;

import org.apache.tomcat.util.bcel.classfile.Constant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.yiqixue.admin.persistence.category.CategoryRepository;
import org.yiqixue.common.book.Category;

@Service
public class CategoryService {
	
	private static final int CATEGORIES_PER_PAGE = 5;
	
	@Autowired
	CategoryRepository categoryRepo;
	
	public List<Category> listAll(){
		return categoryRepo.findAll();
	}
	
	public Category get(Integer id) {
		return categoryRepo.findById(id).get();
	}
	
	public Category save(Category category) {
		Category parent = category.getParent();
		if(parent != null) {
			String allParentIDs = parent.getAllParentIDs() == null? "-" : parent.getAllParentIDs();
			allParentIDs += String.valueOf(parent.getId())+"-";
			category.setAllParentIDs(allParentIDs);
		}
		return categoryRepo.save(category);
	}
	
	public Page<Category> listByPage(int pageNum, int pageSize){
		
		
		Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
		
		//if(keyword != null) return categoryRepo.listByPage(keyword, pageable);
		
		return categoryRepo.findAll(pageable);
	}
	
	public Page<Category> listByPage(int pageNum, int pageSize, String sortField, String sortDir, String keyword){
		
		Pageable pageable;
		
		if(sortField!=null && !sortField.isEmpty()) {
			Sort sort = Sort.by(sortField);
			sort = sortDir.equals("asc")? sort.ascending() : sort.descending();
		
			pageable = PageRequest.of(pageNum, pageSize, sort);
		}else {
			pageable = PageRequest.of(pageNum, pageSize);
		}
		
		if(keyword != null && !keyword.isEmpty()) return categoryRepo.listByPage(keyword, pageable);
		
		return categoryRepo.findAll(pageable);
	}	
	
	public void delete(Integer id) {
		categoryRepo.deleteById(id);
	}
	
}
