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
}
