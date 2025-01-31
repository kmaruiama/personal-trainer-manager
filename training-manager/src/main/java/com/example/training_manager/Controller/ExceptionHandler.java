package com.example.training_manager.Controller;

import com.example.training_manager.Exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> validationExceptionHandler(MethodArgumentNotValidException exception) {
        Map<String, String> errorList = new HashMap<>();

        exception.getBindingResult().getAllErrors().forEach(error -> {
            //variável do DTO
            String fieldName = ((FieldError) error).getField();
            //erro do DTO
            String errorMessage = error.getDefaultMessage();
            errorList.put(fieldName, errorMessage);
        });

        return errorList;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @org.springframework.web.bind.annotation.ExceptionHandler(CustomException.CpfAlreadyExistsException.class)
    public Map<String, String> databaseConflictExceptionHandler(CustomException.CpfAlreadyExistsException exception){
        Map<String, String> errorList = new HashMap<>();
        errorList.put("error", exception.getMessage());
        errorList.put("errorCode", "CPF_ALREADY_EXISTS");
        return errorList;
    }


}
