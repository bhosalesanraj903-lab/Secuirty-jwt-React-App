package com.ansari.spring_security_with_spring_boot.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ansari.spring_security_with_spring_boot.entity.Role;
import com.ansari.spring_security_with_spring_boot.entity.User;
import com.ansari.spring_security_with_spring_boot.request_response.LoginRequest;
import com.ansari.spring_security_with_spring_boot.request_response.LoginResponse;
import com.ansari.spring_security_with_spring_boot.security.jwt.JwtUtils;
import com.ansari.spring_security_with_spring_boot.service.RoleService;
import com.ansari.spring_security_with_spring_boot.service.UserService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    
    private final JwtUtils jwtUtils;

	private final UserService userService;
	
	private final RoleService roleService;
	
	private final PasswordEncoder passwordEncoder;

	public AuthController(AuthenticationManager authenticationManager, JwtUtils jwtUtils, UserService userService,
			RoleService roleService, PasswordEncoder passwordEncoder) {
		super();
		this.authenticationManager = authenticationManager;
		this.jwtUtils = jwtUtils;
		this.userService = userService;
		this.roleService = roleService;
		this.passwordEncoder = passwordEncoder;
	}


	@PostMapping("/register")
	public ResponseEntity<String> saveUserController(@RequestBody User user) {

	    // 1️⃣ Check if username already exists
	    User existingUser = userService.findByUsername(user.getUsername());
	    if (existingUser != null) {
	        return ResponseEntity.badRequest().body("Username Already Exists");
	    }

	    // 2️⃣ Fetch roles from JSON and look up in DB
	    Set<Role> assignedRoles = new HashSet();
	    if (user.getRoles() != null) {
	        for (Role r : user.getRoles()) {
	            // Extract role name from JSON
	            String roleName = r.getName();

	            // Fetch role from DB
	            Role roleFromDB = roleService.getRolebyNameService(roleName); 
	            if (roleFromDB == null) {
	                return ResponseEntity.badRequest().body("Role not found: " + roleName);
	            }
	            assignedRoles.add(roleFromDB);
	        }
	    }

	    // 3️⃣ Assign roles to user
	    user.setRoles(assignedRoles);

	    // 4️⃣ Encode password
	    user.setPassword(passwordEncoder.encode(user.getPassword()));

	    // 5️⃣ Save user
	    User savedUser = userService.saveUserService(user);
	    if (savedUser != null) {
	        return ResponseEntity.ok("User Registered Successfully");
	    }

	    return ResponseEntity.status(500).body("User not Registered !!!!");
	}
	
	
	@PostMapping("/addRole")
	public ResponseEntity<String> saveUserRoleController(@RequestBody Set<Role> role) {
		
		Set<Role> role3 = new HashSet<Role>();
		
		for (Role role2 : role) {
			
			if (roleService.existsByNameService(role2.getName())) {
				return ResponseEntity.badRequest().body("Role Already Exists");
			}
		}
		
		List<Role> role2 = roleService.saveRoleService(role);

		if (role2 != null) {
			return ResponseEntity.ok("Role Saved Successfully");
		}

		return ResponseEntity.ok("Role not Saved !!!!");
	}
	
	@PostMapping("/login")
	public ResponseEntity<LoginResponse> loginController(@RequestBody LoginRequest request) {

	    Authentication authentication = authenticationManager.authenticate(
	            new UsernamePasswordAuthenticationToken(
	                    request.getUsername(),
	                    request.getPassword()
	            )
	    );

	    UserDetails userDetails = (UserDetails) authentication.getPrincipal();

	    String token = jwtUtils.generateJwtToken(userDetails);

	    return ResponseEntity.ok(new LoginResponse(token));
	}
}
