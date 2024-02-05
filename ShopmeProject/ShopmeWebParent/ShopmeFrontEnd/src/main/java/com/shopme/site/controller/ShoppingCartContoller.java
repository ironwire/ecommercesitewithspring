package com.shopme.site.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.site.customer.service.CustomerService;
import com.shopme.site.service.ShoppingCartService;
import com.shopme.site.utility.Utility;

import jakarta.servlet.http.HttpServletRequest;

@Controller
public class ShoppingCartContoller {

	@Autowired
	private ShoppingCartService scservice;
	@Autowired
	private CustomerService cservice;

	@GetMapping("/cart")
	public String viewCartPage(Model model, HttpServletRequest request) {
		Customer customer = getAuthenticatedCustomer(request);
		List<CartItem> cartItems = scservice.listCartItems(customer);
		
		float estimatedTotal = 0.0F;
		for(CartItem item : cartItems){
			estimatedTotal += item.getSubtotal();
		}
		
		model.addAttribute("estimatedTotal", estimatedTotal);
		model.addAttribute("cartItems", cartItems);
		return "/cart/shopping_cart";
	}

	private Customer getAuthenticatedCustomer(HttpServletRequest request) {
		String email = Utility.getEmailOfAuthenticatedCustomer(request);

		return cservice.getCustomerByEmail(email);
	}
}
