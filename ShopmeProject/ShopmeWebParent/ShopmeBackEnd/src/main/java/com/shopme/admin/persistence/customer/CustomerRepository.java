package com.shopme.admin.persistence.customer;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.shopme.common.entity.Customer;


public interface CustomerRepository extends JpaRepository<Customer, Integer> {
	
	@Query("SELECT c FROM Customer c WHERE c.email =:email")
	public Customer getCustomerByEmail(@Param("email") String email);
	
	public Long countById(Integer id);
	
	@Query("UPDATE Customer c SET c.enabled = ?2 WHERE c.id = ?1")
	@Modifying
	public void updateEnabledStatus(Integer id, boolean enabled);
	
	@Query("SELECT c FROM Customer c WHERE "
			+ "CONCAT(c.firstName, ' ', c.lastName, ' ', c.email, ' ', "
			+ "c.addressLine1, ' ', c.addressLine2, ' ', c.city, ' ',"
			+ "  c.state, ' ',  c.country, ' ',  c.postalCode) LIKE %?1%")
	public Page<Customer> findAll(String keyword, Pageable pageable);	

}
