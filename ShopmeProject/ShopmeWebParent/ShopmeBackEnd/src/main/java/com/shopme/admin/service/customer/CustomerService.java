package com.shopme.admin.service.customer;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.shopme.admin.country.persistence.CountryRepository;
import com.shopme.admin.persistence.customer.CustomerRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.User;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerService {

	public static final int CUSTOMERS_PER_PAGE = 10;
	
	@Autowired
	private CustomerRepository customerRepo;
	@Autowired
	private CountryRepository countryRepo;
	@Autowired
	private PasswordEncoder passEncoder;
	
	public Customer getCustomerById(Integer id) throws CustomerNotFoundException{
		try {
			return customerRepo.findById(id).get();
		} catch (NoSuchElementException e) {
			
			throw new CustomerNotFoundException("could not find any customer with ID "+ id);
		}
	}
	
	public Page<Customer> listByPage(int pageNumber, String sortField, String sortDir, String keyword){
		Sort sort= Sort.by(sortField);
		
		sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
		
		Pageable pageable = PageRequest.of(pageNumber-1, CUSTOMERS_PER_PAGE, sort);
		
		if(keyword !=null) {
			return customerRepo.findAll(keyword, pageable);
		}
		
		return customerRepo.findAll(pageable);
	}
	
	public void delete(Integer id) throws CustomerNotFoundException{
		Long countById = customerRepo.countById(id);
		
		if(countById == null || countById == 0) {
			throw new CustomerNotFoundException("Could not find any customer with ID " + id);
			
		}
		customerRepo.deleteById(id);
	}
	
	public Customer save(Customer customer) {
		boolean isUpdatingCustomer = (customer.getId() != null);
		
		if(isUpdatingCustomer) {
			 Customer existingCustomer = customerRepo.findById(customer.getId()).get();
			
			if(customer.getPassword().isEmpty()) {
				customer.setPassword(existingCustomer.getPassword());
			}else {
				encodePassword(customer);
			}
		} else {
			encodePassword(customer);
		}
		return customerRepo.save(customer);
	}

	private void encodePassword(Customer customer) {
		String encoded = passEncoder.encode(customer.getPassword());
		customer.setPassword(encoded);
	}
	
	public void updateCustomerEnabledStatus(Integer id, boolean enabled) {
		customerRepo.updateEnabledStatus(id, enabled);
	}
	
	public boolean isEmailUnique(String email) {
		Customer customer = customerRepo.getCustomerByEmail(email);
		if(customer == null) return true;
		return false;
		
	}
	
	public List<Country> listAllCountries(){
		List<Country> listCountries = countryRepo.findAllByOrderByNameAsc();
		return listCountries;
	}
}
