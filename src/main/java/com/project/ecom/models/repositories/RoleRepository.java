package com.project.ecom.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.project.ecom.models.entities.RoleEntity;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long>{
}
