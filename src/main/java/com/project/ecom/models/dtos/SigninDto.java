package com.project.ecom.models.dtos;

import java.util.Set;

public class SigninDto {
	
	private String username;
	private String email;
	private String password;
	private Set<Long> idRoleSet;
	
	public SigninDto() {}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<Long> getIdRoleSet() {
		return idRoleSet;
	}

	public void setIdRoleSet(Set<Long> idRoleSet) {
		this.idRoleSet = idRoleSet;
	}
}
