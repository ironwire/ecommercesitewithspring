package com.shopme.site.product.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.ProductNotFoundException;
import com.shopme.site.product.persistence.ProductRepository;

@Service
public class ProductService {

	public static final int PRODUCTS_PER_PAGE = 5;
	
	@Autowired private ProductRepository prepo;
	
	public Page<Product> listByCategory(int pageNum, Integer categoryId){
		String categoryIidMatch = "-" + String.valueOf(categoryId) + "-";
		Pageable pageable = PageRequest.of(pageNum -1 , PRODUCTS_PER_PAGE);
		
		return prepo.listByCategory(categoryId, categoryIidMatch, pageable);
		
	}
	
	public Product getProduct(String alias) throws ProductNotFoundException {
		Product product = prepo.findByAlias(alias);
		
		if(product == null) {
			throw new ProductNotFoundException("Could not find product with alias: "+alias);
		}
		return product;
	}
	
	public Page<Product> search(String keyword, int pageNum){
		Pageable pageable = PageRequest.of(pageNum -1 , PRODUCTS_PER_PAGE);
		return prepo.search(keyword, pageable);
	}
}
