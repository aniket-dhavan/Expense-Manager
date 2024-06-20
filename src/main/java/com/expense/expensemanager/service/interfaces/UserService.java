package com.expense.expensemanager.service.interfaces;


import java.util.List;

import com.expense.expensemanager.entity.Budget;
import com.expense.expensemanager.entity.User;
import com.expense.expensemanager.exceptions.MissingValueException;
import com.expense.expensemanager.exceptions.UserNotFoundException;

public interface UserService {
    public User createUser(User user) throws MissingValueException;

    public User updateUser(Long userId,User user) throws UserNotFoundException;

    public String deleteUser(Long userId);

   

    public String setBudget(Long userId,Budget budget);

    public User getUserDetails(Long userId);

    public List<User> getAllUsers();
}
