package com.shopme.admin.controller.customer;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.shopme.admin.service.customer.CountryService;
import com.shopme.admin.service.customer.CustomerNotFoundException;
import com.shopme.admin.service.customer.CustomerService;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;

@Controller
public class CustomerController {

	@Autowired private CustomerService cservice;
	@Autowired private CountryService cc;
	
	@GetMapping("/customers")
	public String listFirstPage(Model model) {
		return listByPage(1, model, "firstName", "asc", null); 
		
	}
	@GetMapping("/customers/page/{pageNum}")
	public String listByPage(@PathVariable(name = "pageNum") int pageNum, Model model,
			@Param("sortField") String sortField, @Param("sortDir") String sortDir,
			@Param("keyword") String keyword) {
		
		Page<Customer> page = cservice.listByPage(pageNum, sortField, sortDir, keyword);
		List<Customer> listCustomers = page.getContent();
		
		long startCount = (pageNum - 1) * CustomerService.CUSTOMERS_PER_PAGE + 1;
		long endCount = startCount + CustomerService.CUSTOMERS_PER_PAGE - 1;
		if(endCount > page.getTotalElements()) {
			endCount = page.getTotalElements();
		}
		
		String reverseSortDir = sortDir.equals("asc") ? "desc" : "asc";
		
		model.addAttribute("currentPage", pageNum);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("startCount", startCount);
		model.addAttribute("endCount", endCount);
		model.addAttribute("totalItems", page.getTotalElements());
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", reverseSortDir);
		model.addAttribute("keyword", keyword);
		model.addAttribute("customers", listCustomers);
		
		return "customers/customers";
	}
	
	@GetMapping("/customers/{id}/enabled/{status}")
	public String udpateCustomerEnabledStatu(
			@PathVariable("id") Integer id,
			@PathVariable("status") boolean enabled, 
			RedirectAttributes red) {
		cservice.updateCustomerEnabledStatus(id, enabled);
		String status = enabled? "enabled":"disabled";
		String message ="The customer with ID "+id+" has been " + status;
		red.addFlashAttribute("message", message);
		return "redirect:/customers";
	}
	
	@GetMapping("/customers/delete/{id}")
	public String deleteCustomer(@PathVariable(name = "id") Integer id, 
			Model model, 
			RedirectAttributes redirectAttributes){
		try {
			cservice.delete(id);
			redirectAttributes.addFlashAttribute("message", "The customer ID "+id+" hase been deleted successfully.");
		}catch(CustomerNotFoundException ex) {
			redirectAttributes.addFlashAttribute("message", ex.getMessage());
		}
		
		return "redirect:/customers";
	}
	
	@GetMapping("/customers/edit/{id}")
	public String editCustomer(@PathVariable("id") Integer id, Model model,
			RedirectAttributes ra) {
		try {
			Customer customer = cservice.getCustomerById(id);
			List<Country> listCountries = cc.listAll();
			
			model.addAttribute("listCountries", listCountries);
			model.addAttribute("customer", customer);
			model.addAttribute("pageTitle", "Edit Customer (ID:"+ id +")");
			
			return "customers/customer_form";
		}catch(CustomerNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/customers";
			
		}
	}
	
	@GetMapping("/customers/detail/{id}")
	public String viewCustomer(@PathVariable("id") Integer id, Model model,
			RedirectAttributes ra) {
		try {
			Customer customer = cservice.getCustomerById(id);
			model.addAttribute("customer", customer);
		
			return "customers/customer_detail_modal";
		}catch(CustomerNotFoundException e) {
			ra.addFlashAttribute("message", e.getMessage());
			return "redirect:/customers";
			
		}
	}
	
	@PostMapping("/customers/save")
	public String saveCustomer(Customer customer, RedirectAttributes redirectAttributes
			) throws IOException {
		
			Customer savedCustomer = cservice.save(customer);
			
		
		redirectAttributes.addFlashAttribute("message", "the customer has been saved successfully.");
		
		String firstName = savedCustomer.getFirstName();
		
		return "redirect:/users/page/1?sortField=id&sortDir=asc&keyword="+firstName;
	}

	
}
