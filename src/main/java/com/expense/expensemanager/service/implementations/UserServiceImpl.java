package com.expense.expensemanager.service.implementations;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.expense.expensemanager.entity.Budget;
import com.expense.expensemanager.entity.User;
import com.expense.expensemanager.exceptions.MissingValueException;
import com.expense.expensemanager.exceptions.UserNotFoundException;

import com.expense.expensemanager.repository.UserRepository;
import com.expense.expensemanager.service.interfaces.BudgetService;
import com.expense.expensemanager.service.interfaces.UserService;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BudgetService budgetService;

  @Override
  @Transactional
  public User createUser(User user) throws MissingValueException {
    if (!Objects.nonNull(user.getName())) {
      throw new MissingValueException("Add User Name to Create New User");
    }
    return userRepository.save(user);
  }

  @Override
  @Transactional
  public User updateUser(Long userId, User user) throws UserNotFoundException {
    Optional<User> dbUser = userRepository.findById(userId);

    if (dbUser.isPresent()) {
      if (Objects.nonNull(user.getName())) {
        dbUser.get().setName(user.getName());
      }
      if (Objects.nonNull(user.getBudget())) {
        dbUser.get().setBudget(user.getBudget());
      }
      if (Objects.nonNull(user.getExpense())) {
        dbUser.get().setExpense(user.getExpense());
        ;
      }
      return userRepository.save(dbUser.get());
    }
    throw new UserNotFoundException("User Not Found For Given Id");
  }

  @Override
  @Transactional
  public String deleteUser(Long userId) {
    Optional<User> dbUser = userRepository.findById(userId);
    if (dbUser.isPresent()) {
      userRepository.delete(dbUser.get());
      return "User Deleted";
    }
    throw new UserNotFoundException("User Not Found For Given Id");
  }

  @Override
  public void resetBudget(User user) {

  }

  @Override
  @Transactional
  public String setBudget(Long userId, Budget budget) {
    Optional<User> user = userRepository.findById(userId);

    LocalDate date =LocalDate.now();

    Optional<Budget> userBudget = budgetService.getBudgetOfMonth(userId, date);

    if (userBudget.isPresent()) {
      return "Budget available for given month";
    }
    if (user.isPresent()) {
      budget.setUserId(userId);
      budget.setDate(date.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli());
      budget.setRemainingBudget(budget.getTotalBudget());
      if (user.get().getBudget() != null) {
        user.get().getBudget().add(budget);
        userRepository.save(user.get());
        return "Updated Budget" + budget.getTotalBudget();
      } else {
        Set<Budget> newBudget = new HashSet<>();
        newBudget.add(budget);
        user.get().setBudget(newBudget);
        userRepository.save(user.get());
        return "Updated Budget" + budget.getTotalBudget();
      }

    }

    userRepository.save(user.get());

    throw new UserNotFoundException("User Not Found With Given Id");
  }

  @Override
  public User getUserDetails(Long userId) {

    Optional<User> user = userRepository.findById(userId);
    if (user.isPresent()) {
      return user.get();
    }

    throw new UserNotFoundException("User not found for given id");
  }

  @Override
  public List<User> getAllUsers() {
    return userRepository.findAll();
  }

}
