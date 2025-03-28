package com.project.ecom.models.entities;

import java.util.Set;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "category")
public class CategoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategory;

    @Column(name = "label", unique = true, nullable = false)
    private String labelCategory;

    @OneToMany(mappedBy = "category")
    @JsonIgnore
    private Set<ExpenseEntity> expenses;

    public CategoryEntity() {}

    public CategoryEntity(String labelCategory) {
        this.labelCategory = labelCategory;
    }

	public Long getIdCategory() {
		return idCategory;
	}

	public void setIdCategory(Long idCategory) {
		this.idCategory = idCategory;
	}

	public String getLabelCategory() {
		return labelCategory;
	}

	public void setLabelCategory(String labelCategory) {
		this.labelCategory = labelCategory;
	}

	public Set<ExpenseEntity> getExpenses() {
		return expenses;
	}

	public void setExpenses(Set<ExpenseEntity> expenses) {
		this.expenses = expenses;
	}

	@Override
	public String toString() {
		return "CategoryEntity [idCategory=" + idCategory + ", labelCategory=" + labelCategory + ", expenses="
				+ expenses + "]";
	}
}
