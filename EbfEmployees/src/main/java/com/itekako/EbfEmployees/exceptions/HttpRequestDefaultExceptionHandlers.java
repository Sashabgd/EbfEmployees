package com.itekako.EbfEmployees.exceptions;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class HttpRequestDefaultExceptionHandlers extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {ResourceNotFoundException.class})
    protected ResponseEntity<Object> handleResourceNotFound(Exception e, WebRequest request){
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", e.getMessage());
        errorResponse.put("status", HttpStatus.NOT_FOUND.toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    protected ResponseEntity<Object> handleResourceAlreadyExists(Exception e, WebRequest request){
        Map<String, String> errorResponse = new HashMap<>();
        DataIntegrityViolationException dataIntegrityViolationException = (DataIntegrityViolationException)e;
        if(dataIntegrityViolationException.getCause() instanceof ConstraintViolationException){
            ConstraintViolationException constraintViolationException =
                    (ConstraintViolationException)dataIntegrityViolationException.getCause();
            switch (constraintViolationException.getConstraintName()){
                case "employees_email_key":
                    errorResponse.put("message", "Employee email already exists!");
                    break;
                case "companies_name_key":
                    errorResponse.put("message", "Company name already exists");
                    break;
                case "salary_nonnegative":
                    errorResponse.put("message", "Employee salary is negative");
                    break;
                default:
                    break;
            }
        }else{
            errorResponse.put("message", "Invalid data!");
        }
        errorResponse.put("status", HttpStatus.BAD_REQUEST.toString());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
}
