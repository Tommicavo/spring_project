package com.project.ecom.restcontrollers;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import com.project.ecom.models.dtos.ErrorDto;
import com.project.ecom.models.dtos.ExpenseDto;
import com.project.ecom.models.entities.ExpenseEntity;
import com.project.ecom.services.ExpenseService;
import com.project.ecom.utils.AppUtils;

@RestController
@RequestMapping("/expenses")
public class ExpenseRestController {
    
    @Autowired
    private ExpenseService expenseService;

	@Autowired
	private AppUtils utils;

    @GetMapping()
    @PreAuthorize("isAuthenticated()")
    @Cacheable(value = "expenses", key = "'all_expenses'")
    public ResponseEntity<?> getAllExpenses() {
        try {
            List<ExpenseEntity> expenses = expenseService.getAllExpenses();
            if (expenses.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto("getAllExpenses", "No expenses found"));
            }
            return ResponseEntity.ok(expenses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("getAllExpenses", "Exception: " + e.getMessage()));
        }
    }

    @GetMapping("/{idExpense}")
    @PreAuthorize("isAuthenticated()")
    @Cacheable(value = "expenses", key = "#idExpense")
    public ResponseEntity<?> getExpense(@PathVariable Long idExpense) {
        try {
            ExpenseEntity expense = expenseService.getExpense(idExpense);
            if (expense == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto("getExpense", "Expense not found"));
            }
            return ResponseEntity.ok(expense);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("getExpense", "Exception: " + e.getMessage()));
        }
    }

    @PostMapping()
    @PreAuthorize("isAuthenticated()")
    @CachePut(value = "expenses", key = "#result.body.idExpense")
    public ResponseEntity<?> insertExpense(@RequestBody ExpenseDto expenseDto) {
        try {
			Long idUser = utils.getIdUserFromSCH(SecurityContextHolder.getContext().getAuthentication());
            ExpenseEntity createdExpense = expenseService.insertExpense(idUser, expenseDto);
            if (createdExpense == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto("insertExpense", "Expense could not be created"));
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(createdExpense);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("insertExpense", "Exception: " + e.getMessage()));
        }
    }

    @PutMapping("/{idExpense}")
    @PreAuthorize("isAuthenticated()")
    @CachePut(value = "expenses", key = "#result.body.idExpense")
    public ResponseEntity<?> updateExpense(@PathVariable Long idExpense, @RequestBody ExpenseDto expenseDto) {
        try {
			Long idUser = utils.getIdUserFromSCH(SecurityContextHolder.getContext().getAuthentication());
            ExpenseEntity updatedExpense = expenseService.updateExpense(idUser, idExpense, expenseDto);
            if (updatedExpense == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto("updateExpense", "Expense not found or update failed"));
            }
            return ResponseEntity.ok(updatedExpense);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorDto("updateExpense", "Exception: " + e.getMessage()));
        }
    }
}
