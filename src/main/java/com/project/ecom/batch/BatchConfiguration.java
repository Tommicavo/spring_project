package com.project.ecom.batch;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import com.project.ecom.models.entities.ReportEntity;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {

    @Autowired
    private ReportReader reader;

    @Autowired
    private ReportProcessor processor;

    @Autowired
    private ReportWriter writer;

    @Autowired
    private ReportListener listener;

    @Bean
    public Job generateReportJob(JobRepository jobRepository, Step generateReportStep) {
        return new JobBuilder("generateReportJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .listener(listener)
                .flow(generateReportStep)
                .end()
                .build();
    }

    @Bean
    public Step generateReportStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("generateReportStep", jobRepository)
                .<ReportEntity, ReportEntity>chunk(1, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}
