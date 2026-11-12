package com.ansari.spring_security_with_spring_boot.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.ansari.spring_security_with_spring_boot.security.jwt.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class MySecurityConfig {

	private final JwtAuthenticationFilter jwtFilter;
	
	public MySecurityConfig(JwtAuthenticationFilter jwtFilter) {
		super();
		this.jwtFilter = jwtFilter;
	}
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		
		http.csrf(csrf->csrf.disable())
		.cors(Customizer.withDefaults())

		.authorizeHttpRequests(auth->auth
				.requestMatchers("/admin/**").hasRole("ADMIN")
				.requestMatchers("/user/**").hasAnyRole("USER")
				.requestMatchers("/api/auth/**").permitAll()
				.anyRequest().authenticated()
				).sessionManagement(session -> session.sessionCreationPolicy(
			            org.springframework.security.config.http.SessionCreationPolicy.STATELESS
				        ))
				        .addFilterBefore(jwtFilter, org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class);
		
		return http.build();
	}
	
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration)throws Exception {
		return configuration.getAuthenticationManager();
	}
}
