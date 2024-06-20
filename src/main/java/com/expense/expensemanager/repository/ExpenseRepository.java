package com.expense.expensemanager.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.expense.expensemanager.entity.Expense;

import java.util.*;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
  @Query(value = "select * from expense e where e.user_id=:userId and e.date=:date", nativeQuery = true)
  public List<Expense> getExpensesByDate(@Param("userId") Long userId, @Param("date") Long date);

  @Query(
    value = "SELECT * FROM expense WHERE user_id = :userId AND date >= (:startDate) AND date <=(:endDate)", 
    nativeQuery = true
    )
  public List<Expense> getExpensesByMonth(@Param("userId") Long userId, @Param("startDate") Long startDate,
      @Param("endDate") Long endDate);



}


