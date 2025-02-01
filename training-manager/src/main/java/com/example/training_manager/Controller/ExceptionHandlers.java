package com.example.training_manager.Controller;

import com.example.training_manager.Exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionHandlers {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
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
    @ExceptionHandler(CustomException.CpfAlreadyExistsException.class)
    public Map<String, String> databaseCpfConflictExceptionHandler(CustomException.CpfAlreadyExistsException exception){
        Map<String, String> errorList = new HashMap<>();
        errorList.put("error", exception.getMessage());
        errorList.put("errorCode", "CPF_ALREADY_EXISTS");
        return errorList;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomException.TrainerNotFound.class)
    public Map<String, String> trainerNotFoundExceptionHandler(CustomException.TrainerNotFound exception){
        Map<String, String> errorList = new HashMap<>();
        errorList.put("error", exception.getMessage());
        errorList.put("errorCode", "TRAINER_NOT_FOUND");
        return errorList;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomException.CustomerNotFound.class)
    public Map<String, String> customerNotFoundExceptionHandler(CustomException.CustomerNotFound exception){
        Map<String, String> errorList = new HashMap<>();
        errorList.put("error", exception.getMessage());
        errorList.put("errorCode", "CUSTOMER_NOT_FOUND");
        return errorList;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomException.InvalidHeader.class)
    public Map<String, String> invalidHeaderExceptionHandler(CustomException.TrainerNotFound exception){
        Map<String, String> errorList = new HashMap<>();
        errorList.put("error", exception.getMessage());
        errorList.put("errorCode", "INVALID_HEADER");
        return errorList;
    }


}
