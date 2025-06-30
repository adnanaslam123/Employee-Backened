package com.example.employeemanagement.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.example.employeemanagement.exception.CustomizeException;

@RestControllerAdvice
public class EmployeeExceptionHandler {

	@ExceptionHandler(CustomizeException.class)
	public ResponseEntity<String> carNotFound(CustomizeException carNotFound) {
		ResponseEntity<String> entity=new ResponseEntity<String>(carNotFound.getMessage(), HttpStatus.NOT_FOUND);
		return entity;
		
	}
	
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        ex.printStackTrace(); // optional: logs the stack trace for debugging
        return new ResponseEntity<>("Something went wrong: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
//spring initializr