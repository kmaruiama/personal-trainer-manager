package com.example.training_manager.Exception;

import org.hibernate.jdbc.Work;

public class CustomException {
    public static class CpfAlreadyExistsException extends RuntimeException {
        public CpfAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class UsernameAlreadyExistsException extends RuntimeException {
        public UsernameAlreadyExistsException(String message) {
            super(message);
        }
    }

    public static class UnauthorizedDataManipulation extends RuntimeException {
        public UnauthorizedDataManipulation (String message) {
            super(message);
        }
    }

    public static class TrainerNotFound extends RuntimeException{
        public TrainerNotFound (String message){
            super(message);
        }
    }

    public static class CustomerNotFound extends RuntimeException{
        public CustomerNotFound (String message){
            super(message);
        }
    }

    public static class InvalidHeader extends RuntimeException{
        public InvalidHeader (String message){
            super(message);
        }
    }

    public static class InvalidCredentials extends RuntimeException{
        public InvalidCredentials (String message){
            super(message);
        }
    }

    public static class RoleAttributionException extends RuntimeException{
        public RoleAttributionException (String message){
            super(message);
        }
    }

    public static class UserNotFoundException extends RuntimeException{
        public UserNotFoundException (String message){
            super(message);
        }
    }

    public static class CannotRetrieveLastCustomerWeightInputException extends RuntimeException{
        public CannotRetrieveLastCustomerWeightInputException(String message){
            super(message);
        }
    }

    public static class CannotRetrieveWeightException extends RuntimeException{
        public CannotRetrieveWeightException(String message){
            super(message);
        }
    }

    public static class PaymentNotFoundException extends RuntimeException{
        public PaymentNotFoundException(String message){
            super(message);
        }
    }

    public static class ScheduleConflictException extends RuntimeException{
        public ScheduleConflictException(String message){
            super(message);
        }
    }

    public static class WorkoutNotFoundException extends RuntimeException{
        public WorkoutNotFoundException(String message){
            super(message);
        }
    }

    public static class ScheduleNotRegisteredYetException extends RuntimeException{
        public ScheduleNotRegisteredYetException(String message){
            super(message);
        }
    }

    public static class ScheduleNotFoundException extends RuntimeException{
        public ScheduleNotFoundException(String message){
            super(message);
        }
    }

    public static class ProgramNotFoundException extends RuntimeException{
        public ProgramNotFoundException(String message){
            super(message);
        }
    }

    public static class ExerciseNotFoundException extends RuntimeException{
        public ExerciseNotFoundException(String message){
            super(message);
        }
    }

    public static class SetNotFoundException extends RuntimeException{
        public SetNotFoundException(String message){
            super(message);
        }
    }

    public static class CustomerTriedToAccessUnauthorizedWorkoutException extends RuntimeException{
        public CustomerTriedToAccessUnauthorizedWorkoutException(String message){
            super(message);
        }
    }

    public static class CustomerTriedToAccessUnauthorizedExerciseException extends RuntimeException{
        public CustomerTriedToAccessUnauthorizedExerciseException(String message){
            super(message);
        }
    }

    public static class CustomerTriedToAccessUnauthorizedSetException extends RuntimeException{
        public CustomerTriedToAccessUnauthorizedSetException(String message){
            super(message);
        }
    }




}
