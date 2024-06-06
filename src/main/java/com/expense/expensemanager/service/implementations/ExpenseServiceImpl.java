package com.expense.expensemanager.service.implementations;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.time.Instant;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.ZoneOffset;

import java.time.temporal.TemporalAdjusters;
import java.util.*;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.logging.slf4j.SLF4JLogger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import com.expense.expensemanager.entity.Expense;
import com.expense.expensemanager.entity.Loss;
import com.expense.expensemanager.entity.Profit;
import com.expense.expensemanager.entity.User;
import com.expense.expensemanager.exceptions.BudgetNotSetException;
import com.expense.expensemanager.exceptions.UserNotFoundException;
import com.expense.expensemanager.repository.ExpenseRepository;
import com.expense.expensemanager.repository.UserRepository;
import com.expense.expensemanager.service.interfaces.ExpenseService;

@Service
public class ExpenseServiceImpl implements ExpenseService {

    @Autowired
    private ExpenseRepository expenseRepository;

    @Autowired
    private UserRepository userRepository;

    private final Logger LOGGER = LoggerFactory.getLogger(SLF4JLogger.class);

    public Expense addExpense(Long userId, Expense expense) throws BudgetNotSetException {

        Optional<User> user = userRepository.findById(userId);

        if (user.isPresent()) {

            if (user.get().getBudgetSet() == false) {
                throw new BudgetNotSetException("Please set budget before adding expenses");
            }
            expense.setDate(LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());

            expense.setUserId(userId);

            Set<Expense> expenseList = user.get().getExpense();

            expenseList.add(expense);

            user.get().setRemainingBudget(user.get().getRemainingBudget() - expense.getAmount());

            if (user.get().getRemainingBudget() < 0) {
                LOGGER.info("Budget exceeds");
            }
            userRepository.save(user.get());
            return expense;

        }
        throw new UserNotFoundException("User Not Found With Given Id   ");
    }

    public List<Expense> getExpensesByDate(Long userId, LocalDate date) {

        Optional<User> dbUser = userRepository.findById(userId);
        List<Expense> expenses = new ArrayList<>();

        if (dbUser.isPresent()) {
            expenses = expenseRepository.getExpensesByDate(userId,
                    date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());
        } else {
            throw new UserNotFoundException("User not Found");
        }

        return expenses;
    }

    public List<Expense> getExpensesByMonth(Long userId, Integer month, Integer year) {

        Long startDate = LocalDate.of(year, month, 1).with(TemporalAdjusters.firstDayOfMonth())
                .atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();

        Long endDate = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth())
                .atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();

        return expenseRepository.getExpensesByMonth(userId, startDate, endDate);
    }

    @Override
    public Map<String, Double> calculateProfitAndLoss(User user) {

        Map<String, Double> profitAndLoss = new HashMap<>();

        List<Profit> profits = user.getProfit();

        for (Profit profit : profits) {
            // If profit is already calculated for this month
            if (profit.getDate().equals(LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli())) {

                profitAndLoss.put("profit", expenseRepository.getProfitByDate(profit.getDate(), user.getId()));
                profitAndLoss.put("loss", expenseRepository.getLossByDate(profit.getDate(), user.getId()));
                return profitAndLoss;
            }
        }

        if (user.getBudgetSet() == false) {
            return profitAndLoss;
        }
        Profit profit = new Profit();

        profit.setDate(LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());
        Double profitOfMonth = calculateProfit(user);
        profit.setTotalProfit(profitOfMonth);

        profit.setUserId(user.getId());
        user.getProfit().add(profit);

        Loss loss = new Loss();

        loss.setDate(LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());

        Double lossOfMonth = calculateLoss(user);

        loss.setTotalLoss(lossOfMonth);

        loss.setUserId(user.getId());
        user.getLoss().add(loss);

        userRepository.save(user);

        profitAndLoss.put("profit", profitOfMonth);
        profitAndLoss.put("loss", lossOfMonth);

        return profitAndLoss;
    }

    private double calculateProfit(User user) {
        if (user.getRemainingBudget() <= 0) {
            return 0.0;
        }

        return user.getRemainingBudget();
    }

    private double calculateLoss(User user) {
        if (user.getRemainingBudget() >= 0) {
            return 0.0;
        }

        return user.getRemainingBudget();
    }

    public void generateReport(User user, Map<String, Double> profitAndLoss) throws IOException {

        Month month = LocalDate.now().getMonth().minus(1);

        Integer year = LocalDate.now().getYear();
        
        Long startDate = LocalDate.of(year, month, 1).with(TemporalAdjusters.firstDayOfMonth())
                .atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();

        Long endDate = LocalDate.of(year, month, 1).with(TemporalAdjusters.lastDayOfMonth())
                .atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli();

        String FILE_PATH = "reports/" + user.getName().toUpperCase() + "-" + month + "-" + year.toString() + ".csv";

        addExpensesToCSV(expenseRepository.getExpensesByMonth(user.getId(), startDate, endDate), FILE_PATH, profitAndLoss,
                user);

    }

    private void addExpensesToCSV(List<Expense> expenses, String FILE_PATH, Map<String, Double> profitAndLoss,
            User user)
            throws IOException {
        try (Writer writer = new FileWriter(FILE_PATH);
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT)) {
            csvPrinter.printRecord("Id", "Date", "Amount", "Category");
            csvPrinter.println();
            for (Expense expense : expenses) {
                LocalDate date = Instant.ofEpochMilli(expense.getDate()).atZone(ZoneId.systemDefault()).toLocalDate();
                csvPrinter.printRecord(expense.getId(), date, expense.getAmount(), expense.getCategory());
            }
            csvPrinter.println();
            csvPrinter.printRecord("Profit", "Loss");

            csvPrinter.printRecord(profitAndLoss.get("profit"), profitAndLoss.get("loss"));

        }
    }

}
