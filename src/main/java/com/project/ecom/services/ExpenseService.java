package com.project.ecom.services;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.project.ecom.models.dtos.ExpenseDto;
import com.project.ecom.models.entities.CategoryEntity;
import com.project.ecom.models.entities.ExpenseEntity;
import com.project.ecom.models.entities.UserEntity;
import com.project.ecom.models.repositories.ExpenseRepository;

@Service
public class ExpenseService {
	
	@Autowired
	private ExpenseRepository expenseRepo;

	@Autowired
	private CategoryService categoryService;

	@Autowired
	private UserService userService;

	public List<ExpenseEntity> getAllExpenses() {
		return expenseRepo.findAll();
	}

	public List<ExpenseEntity> getAllValidExpenses(Long idUser, LocalDate startDateReport, LocalDate endDateReport) {
		YearMonth reportMonth = YearMonth.from(startDateReport);

		return expenseRepo.findAll().stream()
			.filter(expense -> expense.getUser().getIdUser().equals(idUser))
			.filter(expense -> {
				YearMonth startMonthExpense = YearMonth.from(expense.getStartDateExpense());
				YearMonth endMonthExpense = (expense.getEndDateExpense() == null) ? startMonthExpense : YearMonth.from(expense.getEndDateExpense());

				if (expense.getEndDateExpense() == null) {
					return startMonthExpense.equals(reportMonth);
				}

				return (startMonthExpense.compareTo(reportMonth) <= 0 && endMonthExpense.compareTo(reportMonth) >= 0);
			})
			.collect(Collectors.toList());
	}
	
	public ExpenseEntity getExpense(Long idExpense) {
		return expenseRepo.findById(idExpense).orElse(null);
	}

	public ExpenseEntity insertExpense(Long idUser, ExpenseDto expenseDto) {
		ExpenseEntity expense = null;
		if (expenseDto != null) {
			expense = new ExpenseEntity();
			CategoryEntity category = categoryService.getCategory(expenseDto.getIdCategory());
			UserEntity user = userService.getUser(idUser);

			expense.setTitleExpense(expenseDto.getTitle());
			expense.setPriceExpense(expenseDto.getPrice());
			expense.setStartDateExpense(expenseDto.getStartDate());
			expense.setEndDateExpense(expenseDto.getEndDate());
			expense.setCategory(category);
			expense.setUser(user);

			expenseRepo.save(expense);
		}
		
		return expense;
	}

	public ExpenseEntity updateExpense(Long idUser, Long idExpense, ExpenseDto expenseDto) {

		ExpenseEntity expense = getExpense(idExpense);

		if (expense != null && expenseDto != null) {
			CategoryEntity category = categoryService.getCategory(expenseDto.getIdCategory());
			UserEntity user = userService.getUser(idUser);

			expense.setTitleExpense(expenseDto.getTitle());
			expense.setPriceExpense(expenseDto.getPrice());
			expense.setStartDateExpense(expenseDto.getStartDate());
			expense.setEndDateExpense(expenseDto.getEndDate());
			expense.setCategory(category);
			expense.setUser(user);

			expenseRepo.save(expense);
		}

		return expense;
	}
}
