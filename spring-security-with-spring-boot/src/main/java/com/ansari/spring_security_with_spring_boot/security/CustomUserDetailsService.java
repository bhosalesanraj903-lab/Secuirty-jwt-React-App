package com.ansari.spring_security_with_spring_boot.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.ansari.spring_security_with_spring_boot.entity.Role;
import com.ansari.spring_security_with_spring_boot.entity.User;
import com.ansari.spring_security_with_spring_boot.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
	
	private final UserRepository userRepository;
	
	public CustomUserDetailsService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

	    User user = userRepository.findByUsername(username)
	            .orElseThrow(() -> new UsernameNotFoundException(
	                    "User not found with username: " + username));

	    List<Role> roles = new ArrayList<>(user.getRoles());

	    return new org.springframework.security.core.userdetails.User(
	            user.getUsername(),
	            user.getPassword(),
	            roles.stream()
	                 .map(role -> new SimpleGrantedAuthority(role.getName()))
	                 .toList()
	    );
	}

}
