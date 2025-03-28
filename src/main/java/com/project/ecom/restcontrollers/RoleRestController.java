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
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.project.ecom.models.dtos.ErrorDto;
import com.project.ecom.models.entities.RoleEntity;
import com.project.ecom.services.RoleService;

@RestController
@RequestMapping("/roles")
public class RoleRestController {
	
	@Autowired
	private RoleService roleService;

	@GetMapping()
	@PreAuthorize("hasRole('ADMIN')")
	@Cacheable(value = "roles", key = "'all_roles'")
	public ResponseEntity<?> getAllRoles() {
		try {
			List<RoleEntity> roles = roleService.getAllRoles();
			if (roles == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto("getAllRoles" , "Roles not found"));
			}
			return ResponseEntity.ok().body(roles);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("getAllRoles", "Exception: " + e.getMessage()));
		}
	}

	@GetMapping("/{idRole}")
	@PreAuthorize("hasRole('ADMIN')")
	@Cacheable(value = "roles", key = "#idRole")
	public ResponseEntity<?> getRole(@PathVariable Long idRole) {
		try {
			RoleEntity role = roleService.getRole(idRole);
			if (role == null) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto("getRole" , "Role not found"));
			}
			return ResponseEntity.ok().body(role);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("getRole", "Exception: " + e.getMessage()));
		}
	}

    @PostMapping()
	@PreAuthorize("hasRole('ADMIN')")
	@CachePut(value = "roles", key = "#result.body.idRole")
    public ResponseEntity<?> insertRole(@RequestBody RoleEntity roleEntity) {
        try {
            RoleEntity savedRole = roleService.insertRole(roleEntity);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedRole);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("insertRole", "Exception: " + e.getMessage()));
        }
    }

    @PutMapping("/{idRole}")
    @PreAuthorize("hasRole('ADMIN')")
	@CachePut(value = "roles", key = "#result.body.idRole")
    public ResponseEntity<?> updateRole(@PathVariable Long idRole, @RequestBody RoleEntity roleEntity) {
        try {
            RoleEntity updatedRole = roleService.updateRole(idRole, roleEntity);
            if (updatedRole == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto("updateRole", "Role not found"));
            }
            return ResponseEntity.ok(updatedRole);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("updateRole", "Exception: " + e.getMessage()));
        }
    }
}
