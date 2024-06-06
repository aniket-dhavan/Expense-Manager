package com.expense.expensemanager.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import com.expense.expensemanager.entity.User;
import com.expense.expensemanager.exceptions.UserNotFoundException;
import com.expense.expensemanager.service.interfaces.UserService;

import java.util.*;

@RestController
@RequestMapping("/api")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/user")
    public ResponseEntity<User> createUser(@RequestBody User user){
        return ResponseEntity.ok().body(userService.createUser(user));
    }

    @PutMapping("/user/{userId}")
    public ResponseEntity<User> updateUser(@PathVariable("userId") Long userId,@RequestBody User user) throws UserNotFoundException{
        return ResponseEntity.ok().body(userService.updateUser(userId,user));
    }  

    @DeleteMapping("user/{userId}")
    public ResponseEntity<String> deleteUser(@PathVariable("userId") Long userId){
        return ResponseEntity.ok().body(userService.deleteUser(userId));
    }

    @PutMapping("user/set-budget/{userId}/{budget}")
    public ResponseEntity<String> setBudget(@PathVariable("userId") Long userId,@PathVariable("budget") Double budget ){
        return ResponseEntity.ok().body(userService.setBudget(userId, budget));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<User> getUserDetails(@PathVariable("userId") Long userId){
        return ResponseEntity.ok().body(userService.getUserDetails(userId));
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers(){
        return ResponseEntity.ok().body(userService.getAllUsers());
    }
}
