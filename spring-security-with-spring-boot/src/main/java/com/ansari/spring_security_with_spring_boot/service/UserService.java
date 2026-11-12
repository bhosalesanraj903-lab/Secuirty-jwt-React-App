package com.ansari.spring_security_with_spring_boot.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.ansari.spring_security_with_spring_boot.entity.User;
import com.ansari.spring_security_with_spring_boot.repository.RoleRepository;
import com.ansari.spring_security_with_spring_boot.repository.UserRepository;

@Service
public class UserService {

	private final UserRepository userRepository;
	
	private final RoleRepository roleRepository;

	public UserService(UserRepository userRepository, RoleRepository roleRepository) {
		super();
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
	}
	
	public User saveUserService(User user) {
		return userRepository.save(user);
	}
	
	public User findByUsername(String username) {
		Optional<User> optional=userRepository.findByUsername(username);
		
		return optional.isPresent()?optional.get():null;
	}
}
