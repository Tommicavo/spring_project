package com.project.ecom.services;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.ecom.models.dtos.SigninDto;
import com.project.ecom.models.dtos.UserRolesDto;
import com.project.ecom.models.entities.RoleEntity;
import com.project.ecom.models.entities.UserEntity;
import com.project.ecom.models.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;

	@Autowired
	private RoleService roleService;

	public List<UserEntity> getAllUsers() {
		return userRepo.findAll();
	}

	public UserEntity getUser(Long idUser) {
		return userRepo.findById(idUser).orElse(null);
	}

	public UserEntity insertUser(SigninDto signinDto, String encodedPwd) {
		UserEntity user = new UserEntity();

		Set<RoleEntity> roleSet = new HashSet<>();
		for (Long idRole : signinDto.getIdRoleSet()) {
			RoleEntity role = roleService.getRole(idRole);
			roleSet.add(role);
		}

		user.setUsernameUser(signinDto.getUsername());
		user.setEmailUser(signinDto.getEmail());
		user.setPasswordUser(encodedPwd);
		user.setRoles(roleSet);

		return userRepo.save(user);
	}

	public UserEntity getUserByEmail(String email) {
		return userRepo.findUserByEmailUser(email);
	}

	public UserEntity editUserRoles(UserRolesDto userRolesDto) {
		UserEntity user = getUser(userRolesDto.getIdUser());
		Set<RoleEntity> roles = user.getRoles();

		Set<RoleEntity> sentRoles = new HashSet<>();
		for (Long idRole : userRolesDto.getIdRoleSet()) {
			RoleEntity role = roleService.getRole(idRole);
			sentRoles.add(role);
		}

		if (userRolesDto.getMode()) {
			roles.addAll(sentRoles);
		} else {
			roles.removeAll(sentRoles);
		}
		
		user.setRoles(roles);
		return userRepo.save(user);
	}
}
