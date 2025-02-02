package com.example.training_manager.Exception;

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

}
