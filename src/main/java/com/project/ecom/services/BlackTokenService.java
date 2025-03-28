package com.project.ecom.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.ecom.models.entities.BlackTokenEntity;
import com.project.ecom.models.repositories.BlackTokenRepository;

@Service
public class BlackTokenService {
	
	@Autowired
	private BlackTokenRepository tokenRepo;

	public List<BlackTokenEntity> getAllBlackTokens() {
		return tokenRepo.findAll();
	}

	public BlackTokenEntity getBlackToken(Long idBlackToken) {
		return tokenRepo.findById(idBlackToken).orElse(null);
	}

	public BlackTokenEntity insertBlackToken(BlackTokenEntity blackTokenEntity) {
		return tokenRepo.save(blackTokenEntity);
	}

	public void clearBlackTokens() {
		tokenRepo.deleteAll();
	}

	public boolean isTokenBlacklisted(String valueBlackToken) {
		return tokenRepo.existsByValueBlackToken(valueBlackToken);
	}
}
