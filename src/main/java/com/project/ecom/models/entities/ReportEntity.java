package com.project.ecom.models.entities;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "report")
public class ReportEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idReport;

    @Column(name = "title", nullable = false)
    private String titleReport;

	@Column(name = "start_date", nullable = false)
	private LocalDate startDateReport;

	@Column(name = "end_date", nullable = true)
	private LocalDate endDateReport;

	@Column(name = "monthly_cost_by_price", nullable = true)
	private Long monthlyCostByPrice;

	@Lob
	@Column(name = "summary", columnDefinition = "LONGBLOB")
	private byte[] summaryReport;

	@ManyToMany
	@JoinTable(
		name = "expense_report",
		joinColumns = @JoinColumn(name = "id_report"),
		inverseJoinColumns = @JoinColumn(name = "id_expense")
	)
	private List<ExpenseEntity> expenses;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    @JsonIgnore
    private UserEntity user;

    public ReportEntity() {}

	public Long getIdReport() {
		return idReport;
	}

	public void setIdReport(Long idReport) {
		this.idReport = idReport;
	}

	public String getTitleReport() {
		return titleReport;
	}

	public void setTitleReport(String titleReport) {
		this.titleReport = titleReport;
	}

	public LocalDate getStartDateReport() {
		return startDateReport;
	}

	public void setStartDateReport(LocalDate startDateReport) {
		this.startDateReport = startDateReport;
	}

	public LocalDate getEndDateReport() {
		return endDateReport;
	}

	public void setEndDateReport(LocalDate endDateReport) {
		this.endDateReport = endDateReport;
	}

	public Long getMonthlyCostByPrice() {
		return monthlyCostByPrice;
	}

	public void setMonthlyCostByPrice(Long monthlyCostByPrice) {
		this.monthlyCostByPrice = monthlyCostByPrice;
	}

	public byte[] getSummaryReport() {
		return summaryReport;
	}

	public void setSummaryReport(byte[] summaryReport) {
		this.summaryReport = summaryReport;
	}

	public List<ExpenseEntity> getExpenses() {
		return expenses;
	}

	public void setExpenses(List<ExpenseEntity> expenses) {
		this.expenses = expenses;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public Map<Long, Long> getMonthlyCostByCategory() {
		Map<Long, Long> monthlyCostByCategory = new HashMap<>();

		for (ExpenseEntity expense : getExpenses()) {
			Long idCategory = expense.getCategory().getIdCategory();
			Long priceExpense = expense.getPriceExpense();

			monthlyCostByCategory.put(idCategory, monthlyCostByCategory.getOrDefault(idCategory, 0L) + priceExpense);
		}

		return monthlyCostByCategory;
	}

	public Map<Integer, Long> getWeeklyCostByPrice() {
		Map<Integer, Long> weeklyCostByPrice = new HashMap<>();

		LocalDate start = getStartDateReport();

		LocalDate firstMonday = start.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
		
		for (ExpenseEntity expense : getExpenses()) {
			LocalDate expenseDate = expense.getStartDateExpense();

			int weekNumber;
			if (expenseDate.isBefore(firstMonday)) {
				weekNumber = 1;
			} else {
				weekNumber = (int) ChronoUnit.WEEKS.between(firstMonday, expenseDate) + 2;
			}

			weeklyCostByPrice.put(weekNumber, weeklyCostByPrice.getOrDefault(weekNumber, 0L) + expense.getPriceExpense());
		}

		return weeklyCostByPrice;
	}

	@JsonIgnore
	public String getReportCosts() {
		StringBuilder reportCosts = new StringBuilder();
	
		reportCosts.append("Monthly Cost by Price: ").append(getMonthlyCostByPrice()).append("\n");
	
		reportCosts.append("Monthly Cost by Category:\n");
		getMonthlyCostByCategory().forEach((categoryId, cost) -> 
			reportCosts.append("  - Category ID ").append(categoryId).append(": ").append(cost).append("\n")
		);
	
		reportCosts.append("Weekly Cost by Price:\n");
		getWeeklyCostByPrice().forEach((week, cost) -> 
			reportCosts.append("  - Week ").append(week).append(": ").append(cost).append("\n")
		);
	
		return reportCosts.toString();
	}
}
