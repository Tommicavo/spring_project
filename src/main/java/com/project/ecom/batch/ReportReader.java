package com.project.ecom.batch;

import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.project.ecom.models.entities.ReportEntity;
import com.project.ecom.models.repositories.ReportRepository;

@Component
public class ReportReader implements ItemReader<ReportEntity> {

    @Autowired
    private ReportRepository reportRepo;

    private Long idReport;
    private boolean isRead = false;

    public void setIdReport(Long idReport) {
        this.idReport = idReport;
        this.isRead = false;
    }

    @Override
    public ReportEntity read() {
        if (isRead || idReport == null) {
            return null;
        }
        isRead = true;
        return reportRepo.findById(idReport).orElse(null);
    }
}
