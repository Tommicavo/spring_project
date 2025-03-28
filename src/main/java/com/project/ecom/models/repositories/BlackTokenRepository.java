package com.project.ecom.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.project.ecom.models.entities.BlackTokenEntity;

@Repository
public interface BlackTokenRepository extends JpaRepository<BlackTokenEntity, Long>{
	boolean existsByValueBlackToken(String valueBlackToken);
}
