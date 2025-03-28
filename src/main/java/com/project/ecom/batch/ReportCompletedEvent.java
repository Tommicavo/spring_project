package com.project.ecom.batch;

import org.springframework.context.ApplicationEvent;

public class ReportCompletedEvent extends ApplicationEvent {
    private final Long idReport;

    public ReportCompletedEvent(Object source, Long idReport) {
        super(source);
        this.idReport = idReport;
    }

    public Long getIdReport() {
        return idReport;
    }
}

