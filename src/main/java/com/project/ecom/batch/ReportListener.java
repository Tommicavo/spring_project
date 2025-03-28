package com.project.ecom.batch;

import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import com.project.ecom.models.repositories.ReportRepository;

@Component
public class ReportListener implements JobExecutionListener {

    @Autowired
    private ReportRepository reportRepo;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Override
    public void afterJob(JobExecution jobExecution) {
        if (jobExecution.getStatus() == BatchStatus.COMPLETED) {
            Long idReport = jobExecution.getJobParameters().getLong("idReport");

            reportRepo.findById(idReport).ifPresent(report -> {
                if (report.getSummaryReport() != null) {
                    System.out.println("Report PDF successfully generated for idReport: " + idReport);
                    eventPublisher.publishEvent(new ReportCompletedEvent(this, idReport));
                } else {
                    System.err.println("Report PDF missing after job execution for idReport: " + idReport);
                }
            });
        } else {
            System.err.println("Job failed for idReport: " + jobExecution.getJobParameters().getLong("idReport"));
        }
    }
}
