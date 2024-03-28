package org.yiqixue.admin;


import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.yiqixue.admin.service.user.JwtService;
import org.yiqixue.admin.utils.JwtTokenUtil;
import org.yiqixue.common.user.AuthRequest;
import org.yiqixue.common.user.AuthResponse;
import org.yiqixue.common.user.User;

import jakarta.validation.Valid;

@RestController
@CrossOrigin(origins="http://localhost:4200")
public class AuthApi {
    @Autowired AuthenticationManager authManager;
    @Autowired JwtTokenUtil jwtUtil;
    
    @Autowired JwtService jwtService;    
    
//    UUID uuid = UUID.randomUUID();
    
    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody @Valid AuthRequest request) throws Exception{
//        try {
//            Authentication authentication = authManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(
//                            request.getEmail(), request.getPassword())
//            );
//             
//            User user = (User) authentication.getPrincipal();
//            String accessToken = jwtUtil.generateToken(uuid,user);
//            AuthResponse response = new AuthResponse(user.getEmail(), accessToken);
              AuthResponse response = jwtService.createJwtToken(request);
              return ResponseEntity.ok().body(response);
             
//        } catch (BadCredentialsException ex) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//        } catch (Exception e) {
//        	e.printStackTrace();
//        	return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
//			
//		}
    }
}
