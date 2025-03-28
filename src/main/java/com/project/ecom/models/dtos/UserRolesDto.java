package com.project.ecom.models.dtos;

import java.util.Set;

public class UserRolesDto {
	
	private Long idUser;
	private Set<Long> idRoleSet;
	private Boolean mode;

	public UserRolesDto() {}

	public UserRolesDto(Long idUser, Set<Long> idRoleSet, Boolean mode) {
		this.idUser = idUser;
		this.idRoleSet = idRoleSet;
	}

	public Long getIdUser() {
		return idUser;
	}

	public void setIdUser(Long idUser) {
		this.idUser = idUser;
	}

	public Set<Long> getIdRoleSet() {
		return idRoleSet;
	}

	public void setIdRoleSet(Set<Long> idRoleSet) {
		this.idRoleSet = idRoleSet;
	}

	public Boolean getMode() {
		return mode;
	}

	public void setMode(Boolean mode) {
		this.mode = mode;
	}
}
