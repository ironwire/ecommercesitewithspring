package org.yiqixue.admin.utils;

import org.springframework.stereotype.Component;
import org.yiqixue.common.user.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;

import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;


@Component
public class JwtTokenUtil {

	private static final long EXPIRE_DURATION= 24*360*1000;
	
	@Value("${app.jwt.secret}")
	private String SECRET_KEY;
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenUtil.class);

	public String generateToken(UUID jiti, UserDetails user) {
		//Map<String, Object> claims = new HashMap<>();
		//SecretKey key = (SecretKey) getSigningKey();
		//SecretKey key = Jwts.SIG.HS256.key().build();
		//Jwts.SIG.HS512;
		        return Jwts.builder()
		        		.id(jiti.toString())
		                .subject(String.format("%s,%s", ((User) user).getId(), ((User) user).getEmail()))
		                .claim("name", ((User) user).getEmail())
		                .issuedAt(new Date(System.currentTimeMillis()))
		                .expiration(new Date(System.currentTimeMillis() + EXPIRE_DURATION))
		                //.signWith(key)
		                
		                .signWith(getSigningKey(),Jwts.SIG.HS256)
		                .compact();
	}
	  
	private SecretKey getSigningKey() {
	        //byte[] keyBytes = this.SECRET_KEY.getBytes(StandardCharsets.UTF_8);
	        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
			//Jwts.SIG.HS512.key()
	        //SecretKey key = Jwts.SIG.HS256.key().build();
	        //return new SecretKeySpec(keyBytes, "HmacSHA256");
	        
	        return Keys.hmacShaKeyFor(keyBytes);
	    }
	
    public boolean validateAccessToken(String token) {
        try {
//            Jwts.parser().setSigningKey().parseClaimsJws(token);
//            Jwts.parser().setSigningKey(getSigningKey()).
            Jwts.parser()
            .verifyWith(getSigningKey())
            .build()
            .parseSignedClaims(token)
            .getPayload();
            return true;
        } catch (ExpiredJwtException ex) {
            LOGGER.error("JWT expired", ex.getMessage());
        } catch (IllegalArgumentException ex) {
            LOGGER.error("Token is null, empty or only whitespace", ex.getMessage());
        } catch (MalformedJwtException ex) {
            LOGGER.error("JWT is invalid", ex);
        } catch (UnsupportedJwtException ex) {
            LOGGER.error("JWT is not supported", ex);
        } catch (SignatureException ex) {
            LOGGER.error("Signature validation failed");
        }
         
        return false;
    }
	
	
    public String getSubject(String token) {
        return parseClaims(token).getSubject();
    }
     
    private Claims parseClaims(String token) {
//        return Jwts.parser()
//                .setSigningKey(SECRET_KEY)
//                .parseClaimsJws(token)
//                .getBody();
    	 return Jwts.parser()
    			 .verifyWith((SecretKey) getSigningKey())
    			 .build()
    			 .parseSignedClaims(token)
    			 .getPayload();
    }
	
	

}
