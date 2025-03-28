package com.project.ecom.models.entities;

import java.time.LocalDate;
import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "expense")
public class ExpenseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idExpense;

    @Column(name = "title", nullable = false)
    private String titleExpense;

    @Column(name = "price", nullable = false)
    private Long priceExpense;

	@Column(name = "start_date", nullable = false)
	private LocalDate startDateExpense;

	@Column(name = "end_date", nullable = true)
	private LocalDate endDateExpense;

    @ManyToOne
    @JoinColumn(name = "id_user", nullable = false)
    @JsonIgnore
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "id_category", nullable = false)
    private CategoryEntity category;

	@ManyToMany(mappedBy = "expenses")
	@JsonIgnore
	private Set<ReportEntity> reports;

    public ExpenseEntity() {}

	public Long getIdExpense() {
		return idExpense;
	}

	public void setIdExpense(Long idExpense) {
		this.idExpense = idExpense;
	}

	public String getTitleExpense() {
		return titleExpense;
	}

	public void setTitleExpense(String titleExpense) {
		this.titleExpense = titleExpense;
	}

	public Long getPriceExpense() {
		return priceExpense;
	}

	public void setPriceExpense(Long priceExpense) {
		this.priceExpense = priceExpense;
	}

	public LocalDate getStartDateExpense() {
		return startDateExpense;
	}

	public void setStartDateExpense(LocalDate startDateExpense) {
		this.startDateExpense = startDateExpense;
	}

	public LocalDate getEndDateExpense() {
		return endDateExpense;
	}

	public void setEndDateExpense(LocalDate endDateExpense) {
		this.endDateExpense = endDateExpense;
	}

	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	public CategoryEntity getCategory() {
		return category;
	}

	public void setCategory(CategoryEntity category) {
		this.category = category;
	}

	public Set<ReportEntity> getReports() {
		return reports;
	}

	public void setReports(Set<ReportEntity> reports) {
		this.reports = reports;
	}

	@Override
	public String toString() {
		return "ExpenseEntity [idExpense=" + idExpense + ", titleExpense=" + titleExpense + ", priceExpense="
				+ priceExpense + ", startDateExpense=" + startDateExpense + ", endDateExpense=" + endDateExpense
				+ ", category=" + category + "]";
	}
}
