package org.yiqixue.common.store;

import java.sql.Date;

import org.yiqixue.common.IdBasedEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name="books_stores")
public class Store extends IdBasedEntity {
	
	@OneToOne
	@JoinColumn(name = "address_id")
	private StoreAddress address ;

	
	@Column(name="store_name", unique = false, length = 255, nullable = false)
	private String name;
	
	@Column(name="description", unique = false, length = 512, nullable = true)
	private String description;
	
	@Column(name="shipping_term", unique = false, length = 512, nullable = true)
	private String shippingTerm;
	
	@Column(name="terms_of_sale", unique = false, length = 512, nullable = true)
	private String termsOfSale;
	
	
	@Column(length = 128, nullable = false, unique = true)
	private String email;
	
	
	@Column(length = 128, nullable = true, unique = false)
	private String contactPerson;
	
	@Column(name = "created_time", nullable = false, updatable = false)
	private Date createdTime;
	
	@Column(name = "updated_time")
	private Date updatedTime;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getShippingTerm() {
		return shippingTerm;
	}

	public void setShippingTerm(String shippingTerm) {
		this.shippingTerm = shippingTerm;
	}

	public String getTermsOfSale() {
		return termsOfSale;
	}

	public void setTermsOfSale(String termsOfSale) {
		this.termsOfSale = termsOfSale;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContactPerson() {
		return contactPerson;
	}

	public void setContactPerson(String contactPerson) {
		this.contactPerson = contactPerson;
	}

	public Date getCreatedTime() {
		return createdTime;
	}

	public void setCreatedTime(Date createdTime) {
		this.createdTime = createdTime;
	}

	public Date getUpdatedTime() {
		return updatedTime;
	}

	public void setUpdatedTime(Date updatedTime) {
		this.updatedTime = updatedTime;
	}
	

}
