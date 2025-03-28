package com.project.ecom.restcontrollers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.ecom.models.dtos.ErrorDto;
import com.project.ecom.models.dtos.UserRolesDto;
import com.project.ecom.models.entities.UserEntity;
import com.project.ecom.services.UserService;

@RestController
@RequestMapping("/users")
public class UserRestController {
	
	@Autowired
	private UserService userService;

	@GetMapping()
	@PreAuthorize("hasRole('ADMIN')")
	@Cacheable(value = "users", key = "'all_users'")
	public ResponseEntity<?> getAllUsers() {
		try {
			List<UserEntity> users = userService.getAllUsers();
			if (users == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto("getAllUsers" , "Users not found"));
			}
			return ResponseEntity.ok().body(users);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("getAllUsers", "Exception: " + e.getMessage()));
		}
	}

	@GetMapping("/{idUser}")
	@PreAuthorize("hasRole('ADMIN')")
	@Cacheable(value = "users", key = "#idUser")
	public ResponseEntity<?> getUser(@PathVariable Long idUser) {
		try {
			UserEntity user = userService.getUser(idUser);
			if (user == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto("getUser" , "User not found"));
			}
			return ResponseEntity.ok().body(user);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("getUser", "Exception: " + e.getMessage()));
		}
	}

	@PostMapping("/edit-user-roles")
	@PreAuthorize("hasRole('ADMIN')")
	@CachePut(value = "users", key = "#result.body.idUser")
	public ResponseEntity<?> editUserRoles(@RequestBody UserRolesDto userRolesDto) {
		try {
			UserEntity user = userService.editUserRoles(userRolesDto);
			if (user == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto("setRolesToUser" , "User not found"));
			}
			return ResponseEntity.ok().body(user);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("setRolesToUser", "Exception: " + e.getMessage()));
		}
	}
}
