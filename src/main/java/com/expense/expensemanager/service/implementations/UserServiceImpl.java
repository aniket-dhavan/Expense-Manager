package com.expense.expensemanager.service.implementations;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.expense.expensemanager.entity.User;
import com.expense.expensemanager.exceptions.UserNotFoundException;
import com.expense.expensemanager.repository.UserRepository;
import com.expense.expensemanager.service.interfaces.UserService;

@Service
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Override
  public User createUser(User user) {
    if (Objects.nonNull(user.getBudget())) {
      user.setRemainingBudget(user.getBudget());
      user.setBudgetSet(true);
    }
    return userRepository.save(user);
  }

  @Override
  public User updateUser(Long userId, User user) throws UserNotFoundException {
    Optional<User> dbUser = userRepository.findById(userId);

    if (dbUser.isPresent()) {
      if (Objects.nonNull(user.getName())) {
        dbUser.get().setName(user.getName());
      }
      if (Objects.nonNull(user.getBudget()) && user.getBudget() > 0) {
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

    user.setBudget(0.0);
    user.setBudgetSet(false);
    userRepository.save(user);

  }

  @Override
  public String setBudget(Long userId, Double budget) {
    Optional<User> user = userRepository.findById(userId);

    if (user.isPresent()) {
      user.get().setBudget(budget);
      user.get().setBudgetSet(true);
      userRepository.save(user.get());
      return "User Budget Updated to :" + budget;
    }

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
