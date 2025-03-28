package com.project.ecom.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.ecom.models.entities.RoleEntity;
import com.project.ecom.models.repositories.RoleRepository;

@Service
public class RoleService {
	
	@Autowired
	private RoleRepository roleRepo;

	public List<RoleEntity> getAllRoles() {
		return roleRepo.findAll();
	}

	public RoleEntity getRole(Long idRole) {
		return roleRepo.findById(idRole).orElse(null);
	}

	public RoleEntity insertRole(RoleEntity roleEntity) {
		return roleRepo.save(roleEntity);
	}

	public RoleEntity updateRole(Long idRole, RoleEntity roleEntity) {
		RoleEntity role = getRole(idRole);
		if (roleEntity != null) {
			role.setNameRole(roleEntity.getNameRole());
		}
		return roleRepo.save(role);
	}
}
