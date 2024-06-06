package com.expense.expensemanager.controller;

import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.expense.expensemanager.entity.Expense;
import com.expense.expensemanager.exceptions.BudgetNotSetException;
import com.expense.expensemanager.service.interfaces.ExpenseService;

@RestController
@RequestMapping("/api")
public class ExpenseController {

    @Autowired
    private ExpenseService expenseService;

    @PostMapping("/expense/{userId}")
    public ResponseEntity<Expense> addExpense(
            @PathVariable("userId") Long userId,
            @RequestBody Expense expense) throws BudgetNotSetException {
                
        return ResponseEntity.ok().body(expenseService.addExpense(userId, expense));
    
    }

    @GetMapping("/expense/{userId}/{date}")
    public ResponseEntity<List<Expense>> getAllExpensesByDate(
            @PathVariable("userId") Long userId,
            @PathVariable("date") LocalDate localDate) {
                
        return ResponseEntity.ok().body(expenseService.getExpensesByDate(userId, localDate));
    
    }

    @GetMapping("/expense/{userId}/{month}/{year}")
    public ResponseEntity<List<Expense>> getAllExpensesByMonth(
            @PathVariable("userId") Long userId,
            @PathVariable("month") Integer month,
            @PathVariable("year") Integer year) {

        return ResponseEntity.ok().body(expenseService.getExpensesByMonth(userId, month, year));
    
    }
}
