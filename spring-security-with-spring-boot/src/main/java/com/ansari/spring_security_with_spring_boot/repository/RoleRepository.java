package com.ansari.spring_security_with_spring_boot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ansari.spring_security_with_spring_boot.entity.Role;

public interface RoleRepository extends JpaRepository<Role, Long> {
	Optional<Role> findByName(String name);	
	boolean existsByName(String name);
}
