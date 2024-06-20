package com.expense.expensemanager.service.implementations;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expense.expensemanager.entity.Budget;
import com.expense.expensemanager.repository.BudgetRepository;
import com.expense.expensemanager.service.interfaces.BudgetService;


@Service
public class BudgetServiceImpl implements BudgetService {

    @Autowired
    private BudgetRepository budgetRepository;

    @Override
    public Optional<Budget> getBudgetOfMonth(Long userId, LocalDate date) {

        Long startDate = LocalDate.of(date.getYear(), date.getMonthValue(), 1).with(TemporalAdjusters.firstDayOfMonth())
                .atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();

        Long endDate = LocalDate.of(date.getYear(), date.getMonthValue(), 1).with(TemporalAdjusters.lastDayOfMonth())
                .atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();

        return budgetRepository.getBudgetOfMonth(userId, startDate, endDate);
    }

}
