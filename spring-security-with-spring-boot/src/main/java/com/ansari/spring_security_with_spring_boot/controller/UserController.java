package com.ansari.spring_security_with_spring_boot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

	@GetMapping("/dashboard")
	public String dashboard() {
		return "User Dashboard - Access Granted";
	}


	@PutMapping("/profile")
	public String updateProfile() {
		return "User Profile Updated Successfully";
	}

	@GetMapping("/orders")
	public String orders() {
		return "User Orders List";
	}

}
