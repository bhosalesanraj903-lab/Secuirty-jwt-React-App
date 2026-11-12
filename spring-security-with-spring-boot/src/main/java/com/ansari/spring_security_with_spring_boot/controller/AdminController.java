package com.ansari.spring_security_with_spring_boot.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {

	@GetMapping("/dashboard")
	public String adminDashboard() {
		return "Admin Dashboard - Access Granted";
	}

	@GetMapping("/users")
	public String getAllUsers() {
		return "Fetching all users (ADMIN)";
	}

	@DeleteMapping("/user/{id}")
	public String deleteUser(@PathVariable Long id) {
		return "User with ID " + id + " deleted by ADMIN";
	}

	@PostMapping("/create")
	public String createResource() {
		return "Resource created by ADMIN";
	}
}
