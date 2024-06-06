package com.expense.expensemanager.exceptions;

public class BudgetNotSetException extends Exception{
    public BudgetNotSetException(){

    }
    public BudgetNotSetException(String message){
        super(message);
    }
    
}
