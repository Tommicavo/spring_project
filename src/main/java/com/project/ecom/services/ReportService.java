package com.project.ecom.services;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import com.project.ecom.batch.ReportCompletedEvent;
import com.project.ecom.batch.ReportReader;
import com.project.ecom.models.dtos.ReportDto;
import com.project.ecom.models.entities.ExpenseEntity;
import com.project.ecom.models.entities.ReportEntity;
import com.project.ecom.models.entities.UserEntity;
import com.project.ecom.models.repositories.ReportRepository;
import com.project.ecom.utils.AppLogger;

@Service
public class ReportService {
	
	@Autowired
	private ReportRepository reportRepo;

	@Autowired
	private ExpenseService expenseService;

	@Autowired
	private UserService userService;

	@Autowired
	private AppLogger logger;

	@Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job reportJob;

    @Autowired
    private ReportReader reportReader;

    private final Map<Long, CompletableFuture<ReportEntity>> reportFutures = new ConcurrentHashMap<>();

	public List<ReportEntity> getAllReports() {
		return reportRepo.findAll();
	}

	public ReportEntity getReport(Long idReport) {
		return reportRepo.findById(idReport).orElse(null);
	}

	private Long getMonthlyCostByPrice(List<ExpenseEntity> expenses) {
		return expenses.stream()
			.mapToLong(ExpenseEntity::getPriceExpense)
			.sum();
	}

	public ReportEntity insertReport(Long idUser, ReportDto reportDto) {
		ReportEntity report = new ReportEntity();

		UserEntity user = userService.getUser(idUser);
		
		YearMonth yearMonth = YearMonth.of(reportDto.getYear(), reportDto.getMonth());
		LocalDate startDateReport = yearMonth.atDay(1);
		LocalDate endDateReport = yearMonth.atEndOfMonth();

		List<ExpenseEntity> expenses = expenseService.getAllValidExpenses(idUser, startDateReport, endDateReport);

		Long monthlyCostByPrice = getMonthlyCostByPrice(expenses);

		String titleReport = reportDto.getTitle();

		report.setTitleReport(titleReport);
		report.setStartDateReport(startDateReport);
		report.setEndDateReport(endDateReport);
		report.setExpenses(expenses);
		report.setMonthlyCostByPrice(monthlyCostByPrice);
		report.setUser(user);

		ReportEntity savedReport = reportRepo.save(report);

		logger.log.info("REPORT COSTS:\n{}", savedReport.getReportCosts());

		return report;
	}

    public ReportEntity getReportPdf(Long idReport) throws Exception {
        reportReader.setIdReport(idReport);

        JobParameters jobParameters = new JobParametersBuilder()
                .addLong("idReport", idReport)
                .addLong("time", System.currentTimeMillis())
                .toJobParameters();

        CompletableFuture<ReportEntity> future = new CompletableFuture<>();
        reportFutures.put(idReport, future);

        jobLauncher.run(reportJob, jobParameters);

        return future.get();
    }

    @EventListener
    public void handleReportCompleted(ReportCompletedEvent event) {
        Long idReport = event.getIdReport();
        reportRepo.findById(idReport).ifPresent(report -> {
            System.out.println("ReportService received updated ReportEntity for idReport: " + idReport);

            CompletableFuture<ReportEntity> future = reportFutures.remove(idReport);
            if (future != null) {
                future.complete(report);
            }
        });
    }
}
