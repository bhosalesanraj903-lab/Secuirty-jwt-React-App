package com.ansari.spring_security_with_spring_boot.service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.ansari.spring_security_with_spring_boot.entity.Role;
import com.ansari.spring_security_with_spring_boot.repository.RoleRepository;

@Service
public class RoleService {
	
	private final RoleRepository roleRepository;

	public RoleService(RoleRepository roleRepository) {
		super();
		this.roleRepository = roleRepository;
	}
	
	public Role getRolebyNameService(String name) {
		
		Optional<Role> optional=roleRepository.findByName(name);
		
		return optional.isPresent()?optional.get():null;
	}
	
	
	public List<Role> saveRoleService(Set<Role> role) {

		return roleRepository.saveAll(role);
	}
	
	public List<Role> getAllRolesService() {
		return roleRepository.findAll();
	}
	
	public boolean existsByNameService(String name) {
		return roleRepository.existsByName(name);
	}

}
