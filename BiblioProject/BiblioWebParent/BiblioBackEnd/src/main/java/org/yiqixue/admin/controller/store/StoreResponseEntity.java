package org.yiqixue.admin.controller.store;

public class StoreResponseEntity {
	
	private String status;  // registered:confirmation email has been sent out; verified: email confirmed; enabled: store account enalbed; 
	private String url;   //login url; update account information url; etc
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	
	
}
