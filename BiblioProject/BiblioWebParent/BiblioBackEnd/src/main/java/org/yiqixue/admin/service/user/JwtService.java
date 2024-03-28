package org.yiqixue.admin.service.user;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.hibernate.bytecode.internal.bytebuddy.PrivateAccessorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.yiqixue.admin.persistence.user.UserRepository;
import org.yiqixue.admin.utils.JwtTokenUtil;
import org.yiqixue.common.user.AuthRequest;
import org.yiqixue.common.user.AuthResponse;
import org.yiqixue.common.user.User;

@Service
public class JwtService{  // implements UserDetailsService 

	@Autowired
	private JwtTokenUtil jwtTokenUtil; 
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	UUID uuid = UUID.randomUUID();
	public AuthResponse createJwtToken(AuthRequest authRequest) throws Exception {
		String userName = authRequest.getEmail();
		String password = authRequest.getPassword();
		
		authenticate(userName, password);
		
		UserDetails userDetails = userDetailsService.loadUserByUsername(userName);
		String newGeneratedToken = jwtTokenUtil.generateToken(uuid, userDetails);
		
		User user = userRepo.findByEmail(userName).get();
		return new AuthResponse(user, newGeneratedToken);
		
	}
	
//	@Override
//	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        User user = userRepo.findByEmail(username).get();
//
//        if (user != null) {
//            return new org.springframework.security.core.userdetails.User(
//                    user.getEmail(),
//                    user.getPassword(),
//                    getAuthority(user)
//            );
//        } else {
//            throw new UsernameNotFoundException("User not found with username: " + username);
//        }
//	}
//	
//    private Set<SimpleGrantedAuthority> getAuthority(User user) {
//        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
//        user.getRoles().forEach(role -> {
//            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
//        });
//        return authorities;
//    }
    private void authenticate(String userName, String userPassword) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }

}