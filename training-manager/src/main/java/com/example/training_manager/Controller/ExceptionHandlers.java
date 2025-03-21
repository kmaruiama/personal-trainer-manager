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
    public Map<String, String> invalidHeaderExceptionHandler(CustomException.InvalidHeader exception){
        Map<String, String> errorList = new HashMap<>();
        errorList.put("error", exception.getMessage());
        errorList.put("errorCode", "INVALID_HEADER");
        return errorList;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(CustomException.InvalidCredentials.class)
    public Map<String, String> invalidCredentialsExceptionHandler(CustomException.InvalidCredentials exception){
        Map<String, String> errorList = new HashMap<>();
        errorList.put("error", exception.getMessage());
        errorList.put("errorCode", "INVALID_CREDENTIALS");
        return errorList;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomException.UnauthorizedDataManipulation.class)
    public Map<String, String> unauthorizedDataManipulationExceptionHandler(CustomException.UnauthorizedDataManipulation exception){
        Map<String, String> errorList = new HashMap<>();
        errorList.put("error", exception.getMessage());
        errorList.put("errorCode", "UNAUTHORIZED_DATA_MANIPULATION");
        return errorList;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(CustomException.UsernameAlreadyExistsException.class)
    public Map<String, String> usernameAlreadyExistsExceptionHandler(CustomException.UsernameAlreadyExistsException exception){
        Map<String, String> errorList = new HashMap<>();
        errorList.put("error", exception.getMessage());
        errorList.put("errorCode", "USERNAME_ALREADY_EXISTS");
        return errorList;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(CustomException.RoleAttributionException.class)
    public Map<String, String> roleAttributionExceptionHandler(CustomException.RoleAttributionException exception){
        Map<String, String> errorList = new HashMap<>();
        errorList.put("error", exception.getMessage());
        errorList.put("errorCode", "ROLE_ATTRIBUTION_ERROR");
        return errorList;
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(CustomException.UserNotFoundException.class)
    public Map<String, String> userNotFoundExceptionHandler(CustomException.UserNotFoundException exception){
        Map<String, String> errorList = new HashMap<>();
        errorList.put("error", exception.getMessage());
        errorList.put("errorCode", "USER_NOT_FOUND");
        return errorList;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomException.CannotRetrieveLastCustomerWeightInputException.class)
    public Map<String, String> CannotRetrieveLastCustomerWeightInputExceptionHandler(CustomException.CannotRetrieveLastCustomerWeightInputException exception){
        Map<String, String> errorList = new HashMap<>();
        errorList.put("error", exception.getMessage());
        errorList.put("errorCode", "CANNOT_RETRIEVE_LAST_CUSTOMER_WEIGHT_INPUT");
        return errorList;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomException.CannotRetrieveWeightException.class)
    public Map<String, String> CannotRetrieveWeightExceptionHandler(CustomException.CannotRetrieveWeightException exception){
        Map<String, String> errorList = new HashMap<>();
        errorList.put("error", exception.getMessage());
        errorList.put("errorCode", "CANNOT_RETRIEVE_WEIGHT");
        return errorList;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomException.PaymentNotFoundException.class)
    public Map<String, String> CannotRetrieveWeightExceptionHandler(CustomException.PaymentNotFoundException exception){
        Map<String, String> errorList = new HashMap<>();
        errorList.put("error", exception.getMessage());
        errorList.put("errorCode", "CANNOT_RETRIEVE_PAYMENT");
        return errorList;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CustomException.ScheduleConflictException.class)
    public Map<String, String> ScheduleConflictExceptionHandler(CustomException.ScheduleConflictException exception){
        Map<String, String> errorList = new HashMap<>();
        errorList.put("error", exception.getMessage());
        errorList.put("errorCode", "SCHEDULE_CONFLICT");
        return errorList;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomException.WorkoutNotFoundException.class)
    public Map<String, String> WorkoutNotFoundExceptionHandler(CustomException.WorkoutNotFoundException exception)
    {
        Map<String, String> errorList = new HashMap<>();
        errorList.put("error", exception.getMessage());
        errorList.put("errorCode", "WORKOUT_NOT_FOUND");
        return errorList;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomException.ScheduleNotFoundException.class)
    public Map<String, String> ScheduleNotFoundExceptionHandler(CustomException.ScheduleNotFoundException exception)
    {
        Map<String, String> errorList = new HashMap<>();
        errorList.put("error", exception.getMessage());
        errorList.put("errorCode", "SCHEDULE_NOT_FOUND");
        return errorList;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomException.ProgramNotFoundException.class)
    public Map<String, String> ProgramNotFoundExceptionHandler(CustomException.ProgramNotFoundException exception)
    {
        Map<String, String> errorList = new HashMap<>();
        errorList.put("error", exception.getMessage());
        errorList.put("errorCode", "PROGRAM_NOT_FOUND");
        return errorList;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomException.ExerciseNotFoundException.class)
    public Map<String, String> ExerciseNotFoundExceptionHandler(CustomException.ExerciseNotFoundException exception)
    {
        Map<String, String> errorList = new HashMap<>();
        errorList.put("error", exception.getMessage());
        errorList.put("errorCode", "EXERCISE_NOT_FOUND");
        return errorList;
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CustomException.SetNotFoundException.class)
    public Map<String, String> SetNotFoundExceptionHandler(CustomException.SetNotFoundException exception)
    {
        Map<String, String> errorList = new HashMap<>();
        errorList.put("error", exception.getMessage());
        errorList.put("errorCode", "SET_NOT_FOUND");
        return errorList;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(CustomException.CustomerTriedToAccessUnauthorizedWorkoutException.class)
    public Map<String, String> CustomerTriedToAccessUnauthorizedWorkoutExceptionHandler(CustomException.CustomerTriedToAccessUnauthorizedWorkoutException exception)
    {
        Map<String, String> errorList = new HashMap<>();
        errorList.put("error", exception.getMessage());
        errorList.put("errorCode", "UNAUTHORIZED_WORKOUT_REQUEST");
        return errorList;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(CustomException.CustomerTriedToAccessUnauthorizedExerciseException.class)
    public Map<String, String> CustomerTriedToAccessUnauthorizedExerciseExceptionHandler(CustomException.CustomerTriedToAccessUnauthorizedExerciseException exception)
    {
        Map<String, String> errorList = new HashMap<>();
        errorList.put("error", exception.getMessage());
        errorList.put("errorCode", "UNAUTHORIZED_EXERCISE_REQUEST");
        return errorList;
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(CustomException.CustomerTriedToAccessUnauthorizedSetException.class)
    public Map<String, String> CustomerTriedToAccessUnauthorizedSetExceptionHandler(CustomException.CustomerTriedToAccessUnauthorizedSetException exception)
    {
        Map<String, String> errorList = new HashMap<>();
        errorList.put("error", exception.getMessage());
        errorList.put("errorCode", "UNAUTHORIZED_SET_REQUEST");
        return errorList;
    }






}
