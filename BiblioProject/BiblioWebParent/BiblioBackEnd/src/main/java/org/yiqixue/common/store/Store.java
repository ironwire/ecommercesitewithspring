package org.yiqixue.common.store;

import java.util.Date;

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

	@Column(name="password", length = 64, nullable = false)
	private String password;
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	@Column(name="description", unique = false, length = 512, nullable = true)
	private String description;
	
	@Column(name="shipping_term", unique = false, length = 512, nullable = true)
	private String shippingTerm;
	
	@Column(name="terms_of_sale", unique = false, length = 512, nullable = true)
	private String termsOfSale;
	
	@Column(length = 128, nullable = false, unique = true)
	private String email;

	private boolean enabled;
	
	private boolean emailConfirmed;
	
	public boolean isEmailConfirmed() {
		return emailConfirmed;
	}

	public void setEmailConfirmed(boolean emailConfirmed) {
		this.emailConfirmed = emailConfirmed;
	}

	public StoreAddress getAddress() {
		return address;
	}

	public void setAddress(StoreAddress address) {
		this.address = address;
	}

	public String getVerificationCode() {
		return verificationCode;
	}

	public void setVerificationCode(String verificationCode) {
		this.verificationCode = verificationCode;
	}

	@Column(name = "verification_code", length = 64)
	private String verificationCode;
	
	@Column(name = "first_name", length = 45, nullable = false)
	private String firstName;
	
	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@Column(name = "last_name", length = 45, nullable = false)
	private String lastName;
	
	@Column(name = "created_time", nullable = true, updatable = false)
	private Date createdTime;
	
	@Column(name = "updated_time")
	private Date updatedTime;

	public Store() {}
	
	
	
	public Store(Integer id, String name) {
		super();
		this.name = name;
		this.id = id;
	}

	public Store(Integer id) {
		this.id = id;
	}


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
