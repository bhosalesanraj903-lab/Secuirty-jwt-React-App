package com.ansari.spring_security_with_spring_boot.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ansari.spring_security_with_spring_boot.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
 Optional<User>	findByUsername(String username);
}
