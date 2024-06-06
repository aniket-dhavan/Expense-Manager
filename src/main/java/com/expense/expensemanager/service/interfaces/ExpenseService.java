package com.expense.expensemanager.service.interfaces;

import java.io.IOException;
import java.time.LocalDate;

import java.util.*;

import com.expense.expensemanager.entity.Expense;
import com.expense.expensemanager.entity.User;
import com.expense.expensemanager.exceptions.BudgetNotSetException;

public interface ExpenseService {

    public Expense addExpense(Long userId,Expense expense) throws BudgetNotSetException;

    public List<Expense> getExpensesByDate(Long userId,LocalDate date);

    public List<Expense> getExpensesByMonth(Long userId,Integer month,Integer year);

    public Map<String,Double> calculateProfitAndLoss(User user);

    public void generateReport(User user,Map<String,Double> profitAndLoss) throws IOException;




}
