package com.shopme.admin.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices.RememberMeTokenAlgorithm;

@Configuration
//@EnableWebSecurity
public class WebSecurityConfig {

	@Bean
	public UserDetailsService userDetailsService() {
		return new ShopmeUserDetailsService();
	}
	
	
	@Bean
	public AuthenticationManager authenticationManager(
			UserDetailsService userDetailsService,
			PasswordEncoder passwordEncoder) {
		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
		authenticationProvider.setUserDetailsService(userDetailsService);
		authenticationProvider.setPasswordEncoder(passwordEncoder);

		return new ProviderManager(authenticationProvider);
	}

	
	@Bean
	  SecurityFilterChain filterChain(HttpSecurity http, RememberMeServices rememberMeServices) throws Exception {
	        http
	            .authorizeHttpRequests((authz) -> authz
	            		.requestMatchers("/product-images/**").hasAnyAuthority("Admin","Editor","Salesperson","Shipper")
	                .requestMatchers("/users","/users/**").hasAuthority("Admin")
	                .requestMatchers("/categories/**").hasAnyAuthority("Admin","Editor")
	                .requestMatchers("/brands/**").hasAnyAuthority("Admin","Editor")
	                .requestMatchers("/products/**").hasAnyAuthority("Admin","Editor","Salesperson","Shipper")
	                .requestMatchers("/questions/**").hasAnyAuthority("Admin","Assistant")
	                .requestMatchers("/reviews/**").hasAnyAuthority("Admin","Assistant")
	                .requestMatchers("/customers/**").hasAnyAuthority("Admin","Salesperson")
	                .requestMatchers("/shipping/**").hasAnyAuthority("Admin","Salesperson")
	                .requestMatchers("/orders/**").hasAnyAuthority("Admin","Salesperson","Shipper")
	                .requestMatchers("/reports/**").hasAnyAuthority("Admin","Salesperson")
	                .requestMatchers("/articles/**").hasAnyAuthority("Admin","Editor")
	                .requestMatchers("/menus/**").hasAnyAuthority("Admin","Editor")
	                .requestMatchers("/settings/**").hasAnyAuthority("Admin")
	                .anyRequest().authenticated()
	            		);
	        http.formLogin(formLogin->formLogin.loginPage("/login")
	        		.usernameParameter("email")
	        		.permitAll());
	        http.rememberMe((remember)->remember.rememberMeServices(rememberMeServices));
	        //http.logout((logout) -> logout.logoutUrl("/flash"));
	        //http.logout((logout) -> logout.logoutUrl("/flash").invalidateHttpSession(true).deleteCookies("JSESSIONID"));
	        
	        http
	        .logout(logout -> logout                                                
	            .logoutUrl("/logout")                                            
	            .logoutSuccessUrl("/flash").permitAll()                                      
	            .invalidateHttpSession(true)                                        
	            .deleteCookies()                                  
	        );

	        
	        return http.build();
	    }
   
	@Bean
	RememberMeServices rememberMeServices(UserDetailsService userDetailsService) {
		RememberMeTokenAlgorithm encodingAlgorithm = RememberMeTokenAlgorithm.SHA256;
		TokenBasedRememberMeServices rememberMe = new TokenBasedRememberMeServices("momingqimiao", userDetailsService, encodingAlgorithm);
		rememberMe.setMatchingAlgorithm(RememberMeTokenAlgorithm.MD5);
		return rememberMe;
	}
	  
	
	@Bean
	  public PasswordEncoder passwordEncoder() {
		  return new BCryptPasswordEncoder();
	  }

    @Bean
    WebSecurityCustomizer webSecurityCustomizer() {
		  return (web)->web.ignoring().requestMatchers("/images/**","/js/**","/webjars/**");
	  } 

}
