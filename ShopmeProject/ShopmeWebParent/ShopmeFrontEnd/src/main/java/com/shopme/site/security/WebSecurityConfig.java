package com.shopme.site.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.RememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices.RememberMeTokenAlgorithm;

import com.shopme.site.security.oauth.CustomerOAuth2UserService;
import com.shopme.site.security.oauth.OAuth2LoginSuccessHandler;

@Configuration

public class WebSecurityConfig {

	@Autowired
	private CustomerOAuth2UserService oauthService;
	@Autowired
	private OAuth2LoginSuccessHandler oauth2LoginHandler;
	@Autowired
	private DatabaseLoginSuccessHandler dbSuccessHandler;

	@Bean
	UserDetailsService userDetailsService() {
		return new CustomerUserDetailsService();
	}

//	
//	@Bean
//	AuthenticationManager authenticationManager(UserDetailsService userDetailsService,
//			PasswordEncoder passwordEncoder) {
//
//		DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
//		authenticationProvider.setUserDetailsService(userDetailsService);
//		authenticationProvider.setPasswordEncoder(passwordEncoder);
//
//		return new ProviderManager(authenticationProvider);
//	}

	@Bean
	SecurityFilterChain filterChain(HttpSecurity http, RememberMeServices rememberMeServices) throws Exception {
		http.authorizeHttpRequests(
				(authz) -> authz.requestMatchers("/customers", "/cart", "/account_deteails", "/udpate_account_details")
						.authenticated().anyRequest().permitAll());
		http.formLogin(formLogin -> formLogin.loginPage("/login")

				.usernameParameter("email").permitAll().successHandler(dbSuccessHandler));
		http.oauth2Login(oauth2 -> oauth2.userInfoEndpoint(userInfo -> userInfo.userService(oauthService))
				.loginPage("/login").userInfoEndpoint(userInfo -> userInfo.userService(oauthService))
				.successHandler(oauth2LoginHandler));
		http.logout(logout -> logout.permitAll());
		http.sessionManagement(session->session.sessionCreationPolicy(SessionCreationPolicy.ALWAYS));
		http.rememberMe((remember) -> remember.rememberMeServices(rememberMeServices));
		return http.build();
	}

	@Bean
	RememberMeServices rememberMeServices(UserDetailsService userDetailsService) {
		RememberMeTokenAlgorithm encodingAlgorithm = RememberMeTokenAlgorithm.SHA256;
		TokenBasedRememberMeServices rememberMe = new TokenBasedRememberMeServices("momingqimiao", userDetailsService,
				encodingAlgorithm);
		rememberMe.setMatchingAlgorithm(RememberMeTokenAlgorithm.MD5);
		return rememberMe;
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	WebSecurityCustomizer webSecurityCustomizer() {
		return (web) -> web.ignoring().requestMatchers("/images/**", "/js/**", "/webjars/**");
	}

}
