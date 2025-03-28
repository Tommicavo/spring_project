package com.project.ecom.models.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.project.ecom.models.entities.ReportEntity;

@Repository
public interface ReportRepository extends JpaRepository<ReportEntity, Long>{
}
