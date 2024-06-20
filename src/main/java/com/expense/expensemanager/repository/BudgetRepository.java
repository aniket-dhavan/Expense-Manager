package com.expense.expensemanager.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.expense.expensemanager.entity.Budget;


@Repository
public interface BudgetRepository extends JpaRepository<Budget,Long> {
    
  @Query(
    value = "SELECT * FROM budget WHERE user_id = :userId AND date >= (:startDate) AND date <=(:endDate)", 
    nativeQuery = true
    )
    public Optional<Budget> getBudgetOfMonth(Long userId,Long startDate,Long endDate);
}
