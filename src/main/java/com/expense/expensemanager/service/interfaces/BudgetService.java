package com.expense.expensemanager.service.interfaces;

import java.time.LocalDate;
import java.util.Optional;

import com.expense.expensemanager.entity.Budget;

public interface BudgetService {
    
    public Optional<Budget> getBudgetOfMonth(Long userId,LocalDate date);
}
