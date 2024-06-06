package com.expense.expensemanager.scheduler;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.expense.expensemanager.entity.User;
import com.expense.expensemanager.service.interfaces.ExpenseService;
import com.expense.expensemanager.service.interfaces.UserService;


@EnableScheduling
@Component
public class BudgetScheduler {

    @Autowired
    private UserService userService;

    @Autowired
    private ExpenseService expenseService;

    @Scheduled(fixedRate = 1,timeUnit = TimeUnit.DAYS)
    public void resetBudget() throws IOException{
    
        if(LocalDate.now().getDayOfMonth()==6){
            List<User> allUsers=userService.getAllUsers();

            for(User user:allUsers){
                Map<String,Double>profitAndLoss=expenseService.calculateProfitAndLoss(user);
                expenseService.generateReport(user,profitAndLoss);
                userService.resetBudget(user);
            }
        }
    }
    
}
