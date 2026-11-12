package com.ansari.spring_security_with_spring_boot.security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtils {

	private final String jwtSecretKey = "MySuperSecretKeyForJwtGeneration12345";

	private final long jwtExpirationMs = 86400000; // 24 hours

	private Key getSigningKey() {
		return Keys.hmacShaKeyFor(jwtSecretKey.getBytes());
	}

	// generate token
	public String generateJwtToken(UserDetails userDetails) {

		List<String> roles = userDetails.getAuthorities().stream().map(a -> a.getAuthority())
				.collect(Collectors.toList());
		
		return Jwts.builder()
		.setSubject(userDetails.getUsername())
		.claim("roles", roles)
		.setIssuedAt(new Date())
		.setExpiration(new Date((new Date()).getTime()+jwtExpirationMs))
		.signWith(getSigningKey(),SignatureAlgorithm.HS256)
		.compact();
	}
	
	//getUserName From Token
	public  String getUserNameFromJwtToken(String token) {
		
		return Jwts.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	
	//validate token
	public boolean validateJwtToken(String token) {
		
		try {
			
			Jwts.parserBuilder().setSigningKey(getSigningKey()).build().parseClaimsJws(token);
			
			return true;
			
		} catch (JwtException | IllegalArgumentException e) {
			System.out.println("Invalid JWT token: " + e.getMessage());
			e.printStackTrace();
			return false;
		}
	}


}
