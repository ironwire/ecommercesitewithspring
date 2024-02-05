package com.shopme.admin.product.service;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.shopme.admin.product.persistence.ProductRepository;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ProductNotFoundException;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class ProductService {

	public static final int PRODUCTS_PER_PAGE = 5;
	
	@Autowired
	private ProductRepository prepo;
	
	
	public List<Product> listAllProducts(){
		
		return (List<Product>)prepo.findAll();
	}
	
	public Page<Product> listByPage(int pageNum, String sortField, String sortDir, String keyword){
		Sort sort = Sort.by(sortField);
		
		sort = sortDir.equals("asc")? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE, sort);
		
		if(keyword != null) return prepo.findAll(keyword, pageable);
		
		return prepo.findAll(pageable);
	}
	
	public Product save(Product product) {
		if(product.getId() == null) {
			product.setCreatedTime(new Date());
		}
		
		if (product.getAlias() == null || product.getAlias().isEmpty()) {
			String defaultAlias = product.getName().replaceAll(" ", "-");
			product.setAlias(defaultAlias);
		}else {
			product.setAlias(product.getAlias().replaceAll(" ","-"));
		}
		
		product.setUpdatedTime(new Date());
		
		return prepo.save(product);
	}
	
	public String checkUnique(Integer id, String name) {
		boolean isCreatingNew = (id == null || id == 0);
		Product productByName = prepo.findByName(name);
		
		if (isCreatingNew) {
			if (productByName != null) return "Duplicate";
		} else {
			if (productByName != null && productByName.getId() != id) {
				return "Duplicate";
			}
		}
		
		return "OK";
	}
	
	public void updateProductEnabledStatus(Integer id, boolean enabled) {
		prepo.updateEnabledStatus(id, enabled);
	}
	
	public void deleteProductById(Integer id) throws ProductNotFoundException {
		
		Long countById = prepo.countById(id);
		
		if(countById == null || countById == 0) {
			throw new ProductNotFoundException("Could not find any product with ID " + id);
			
		}
		prepo.deleteById(id);
	}
	
	public Product get(Integer id) throws ProductNotFoundException {
		try {
			return prepo.findById(id).get();
			
		}catch(NoSuchElementException ex) {
			throw new ProductNotFoundException("Could not find any product With ID " + id);
		}
	}
	
	
	
}
