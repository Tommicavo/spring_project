package com.project.ecom.utils;

import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.project.ecom.models.entities.RoleEntity;
import com.project.ecom.models.entities.UserEntity;

/*
Usage in RestControllers:

@Autowired
private AppUtils utils;

Long idUser = utils.getIdFromSCH(SecurityContextHolder.getContext().getAuthentication());
Set<RoleEntity> roles = utils.getRolesFromSCH(SecurityContextHolder.getContext().getAuthentication());

*/

@Component
public class AppUtils {

	@Autowired
	private AppLogger logger;
	
	public Long getIdUserFromSCH(Authentication auth) {
		try {
			return ((UserEntity) auth.getPrincipal()).getIdUser();
		} catch (Exception e) {
			logger.log.error("Exception in getIdFromSCH: " + e.getMessage());
			return null;
		}
	}

	public Set<RoleEntity> getRolesFromSCH(Authentication auth) {
		try {
			return ((UserEntity) auth.getPrincipal()).getRoles();
		} catch (Exception e) {
			logger.log.error("Exception in getRolesFromSCH: " + e.getMessage());
			return null;
		}
	}
}
