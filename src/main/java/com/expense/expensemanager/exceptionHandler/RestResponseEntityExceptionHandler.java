package com.expense.expensemanager.exceptionHandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.expense.expensemanager.exceptions.BudgetNotSetException;
import com.expense.expensemanager.exceptions.MissingValueException;
import com.expense.expensemanager.exceptions.UserNotFoundException;
@ControllerAdvice
@ResponseStatus
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessage> noSuchSupplierException(UserNotFoundException exception,
            WebRequest request) {
        ErrorMessage message = new ErrorMessage(HttpStatus.NOT_FOUND, exception.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(message);
    }

    @ExceptionHandler(BudgetNotSetException.class)
    public ResponseEntity<ErrorMessage> budgetNotsetException(BudgetNotSetException exception,WebRequest request){
        ErrorMessage message=new ErrorMessage(HttpStatus.FAILED_DEPENDENCY,exception.getMessage());

        return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body(message);
    }

    @ExceptionHandler(MissingValueException.class)
    public ResponseEntity<ErrorMessage> missingValueException(MissingValueException exception,WebRequest request){
        ErrorMessage message=new ErrorMessage(HttpStatus.FAILED_DEPENDENCY,exception.getMessage());

        return ResponseEntity.status(HttpStatus.FAILED_DEPENDENCY).body(message);
    }
    
}
