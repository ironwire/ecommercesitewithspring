package com.shopme.site.product;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;
import com.shopme.common.exception.CategoryNotFoundException;
import com.shopme.common.exception.ProductNotFoundException;
import com.shopme.site.category.service.CategoryService;
import com.shopme.site.product.service.ProductService;

@Controller
public class ProductController {

	@Autowired
	private ProductService pservice;
	@Autowired
	private CategoryService cservice;

	@GetMapping("/c/{category_alias}")
	public String viewProductFirstPage(@PathVariable("category_alias") String alias, Model model) {
		return viewProductByCategory(alias, 1, model);
	}

	@GetMapping("/c/{category_alias}/page/{pageNum}")
	public String viewProductByCategory(@PathVariable("category_alias") String alias,
			@PathVariable("pageNum") int pageNum, Model model) {

		try {
			Category ctgr = cservice.getCategory(alias);

			List<Category> listCategoryParents = cservice.getCategoryParents(ctgr);
			Page<Product> pageProducts = pservice.listByCategory(pageNum, ctgr.getId());
			List<Product> listProducts = pageProducts.getContent();

			long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
			long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
			if (endCount > pageProducts.getTotalElements()) {
				endCount = pageProducts.getTotalElements();
			}

			model.addAttribute("currentPage", pageNum);
			model.addAttribute("totalPages", pageProducts.getTotalPages());
			model.addAttribute("startCount", startCount);
			model.addAttribute("endCount", endCount);
			model.addAttribute("totalItems", pageProducts.getTotalElements());

			model.addAttribute("category", ctgr);
			model.addAttribute("pageTitle", ctgr.getName());
			model.addAttribute("listCategoryParents", listCategoryParents);
			model.addAttribute("listProducts", listProducts);

			return "product/product_by_category";

		} catch (CategoryNotFoundException ex) {
			return "error/404";
		}

	}
	
	@GetMapping("/p/{product_alias}")
	public String viewProductDetail(@PathVariable("product_alias") String alias, Model model) {
		
		try {
			Product product= pservice.getProduct(alias);
			List<Category> listCategoryParents = cservice.getCategoryParents(product.getCategory());
			model.addAttribute("product", product);
			model.addAttribute("listCategoryParents", listCategoryParents);
			model.addAttribute("pageTitle", product.getShortName());
			
			return "product/product_detail";
		} catch (ProductNotFoundException e) {
			// TODO Auto-generated catch block
			return "error/404";
		}
	}
	
	@GetMapping("/search")
	public String searchFirstPage(@Param("keyword") String keyword, Model model) {
		return searchByPage(keyword, 1, model);
	}
	
	@GetMapping("/search/page/{pageNum}")
	public String searchByPage(@Param("keyword") String keyword, 
			@PathVariable("pageNum") int pageNum,
			Model model) {
		
		Page<Product> pageProducts = pservice.search(keyword, pageNum);
		List<Product> listResult = pageProducts.getContent();
	
		
		long startCount = (pageNum - 1) * ProductService.PRODUCTS_PER_PAGE + 1;
		long endCount = startCount + ProductService.PRODUCTS_PER_PAGE - 1;
		if (endCount > pageProducts.getTotalElements()) {
			endCount = pageProducts.getTotalElements();
		}

		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", pageProducts.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", pageProducts.getTotalElements());

		
		model.addAttribute("pageTitle", keyword + " - Search Result");
		model.addAttribute("listResult", listResult);
		model.addAttribute("keyword", keyword);
	
		return "product/search_result";
		
	}
}
