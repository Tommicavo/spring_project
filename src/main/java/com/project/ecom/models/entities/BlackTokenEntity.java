package com.project.ecom.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "black_token")
public class BlackTokenEntity {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idBlackToken;

	@Column(name = "token", nullable = false)
	private String valueBlackToken;

	public BlackTokenEntity() {}
	
	public BlackTokenEntity(String valueBlackToken) {
		this.valueBlackToken = valueBlackToken;
	}

	public Long getIdBlackToken() {
		return idBlackToken;
	}

	public void setIdBlackToken(Long idBlackToken) {
		this.idBlackToken = idBlackToken;
	}

	public String getValueBlackToken() {
		return valueBlackToken;
	}
	
	public void setValueBlackToken(String valueBlackToken) {
		this.valueBlackToken = valueBlackToken;
	}
}
