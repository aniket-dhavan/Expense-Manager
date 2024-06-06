package com.expense.expensemanager.entity;


import java.util.List;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Double budget;

    private double remainingBudget;

    private Boolean budgetSet;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Set<Expense> expense;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Profit> profit;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Loss> loss;

}
