package com.project.ecom.batch;

import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;
import com.project.ecom.models.entities.ReportEntity;

@Component
public class ReportProcessor implements ItemProcessor<ReportEntity, ReportEntity> {

    @Override
    public ReportEntity process(ReportEntity report) throws Exception {        
        return report;
    }
}
