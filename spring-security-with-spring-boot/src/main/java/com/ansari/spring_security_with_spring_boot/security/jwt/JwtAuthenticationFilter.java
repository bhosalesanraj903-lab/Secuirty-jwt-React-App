package com.ansari.spring_security_with_spring_boot.security.jwt;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

	private final JwtUtils jwtUtils;
	private final UserDetailsService userDetailsService;

	public JwtAuthenticationFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
		super();
		this.jwtUtils = jwtUtils;
		this.userDetailsService = userDetailsService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
	        throws ServletException, IOException {

	    String path = request.getServletPath();

	    // 🔹 Skip login & register endpoints
	    if (path.startsWith("/api/auth/")) {
	        filterChain.doFilter(request, response);
	        return;
	    }

	    String authHeader = request.getHeader("Authorization");
	    String username = null;
	    String token = null;

	    if (authHeader != null && authHeader.startsWith("Bearer ")) {
	        token = authHeader.substring(7);

	        if (jwtUtils.validateJwtToken(token)) {
	            username = jwtUtils.getUserNameFromJwtToken(token);
	        }
	    }

	    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
	        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

	        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
	                userDetails, null, userDetails.getAuthorities());

	        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
	        SecurityContextHolder.getContext().setAuthentication(authToken);
	    }

	    filterChain.doFilter(request, response);
	}

}
