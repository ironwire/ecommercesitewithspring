package org.yiqixue.common.user;

public class AuthResponse {
	private User user;
	private String accessToken;
	
	private String email;
	
	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public AuthResponse() {}
	

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public AuthResponse(User user, String accessToken) {
		this.user = user;
		this.accessToken = accessToken;
		
	}

	public AuthResponse(String email, String accessToken) {
		this.email = email;
		this.accessToken = accessToken;
		
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}
