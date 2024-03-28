package org.yiqixue.admin;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.yiqixue.admin.persistence.user.UserRepository;



@Configuration

public class ApplicationSecurity {

	@Autowired
	private UserRepository userRepo;
	@Autowired
	private JwtTokenFilter jwtTokenFilter;
	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntrypoint;
	
	//@Autowired
//    private UserDetailsService jwtService;

    
	@Bean
	public UserDetailsService userDetailsService() {
		return new UserDetailsService() {

			@Override
			public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
				return userRepo.findByEmail(username)
						.orElseThrow(() -> new UsernameNotFoundException("User " + username + " not found"));
			}
		};
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
		return authConfig.getAuthenticationManager();
	}

	@Bean
	SecurityFilterChain configure(HttpSecurity http) throws Exception {
		//http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry -> authorizationManagerRequestMatcherRegistry.requestMatchers("/**").permitAll());
		//http.authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
		
		http.authorizeHttpRequests((auth) -> 
			auth.requestMatchers("/registerNewUser","/createNewRole","/auth/login/**",
					"/auth/**","/api/stores/register","/api/stores/checkEmailUniqueness",
					"/api/stores/verify").permitAll()
//            .requestMatchers(org.springframework.http.HttpHeaders.ALLOW).permitAll()
			.anyRequest().authenticated());
//		
		http.csrf((csrf) -> csrf.disable());
//
		http.exceptionHandling((ex) -> ex.authenticationEntryPoint(jwtAuthenticationEntrypoint));
		http.sessionManagement((management)->management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
		http.addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}
	//新增

//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
//        authenticationManagerBuilder.userDetailsService(jwtService).passwordEncoder(passwordEncoder());
//    }
}
