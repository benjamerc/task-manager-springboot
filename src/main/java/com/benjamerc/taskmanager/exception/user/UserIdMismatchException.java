package com.benjamerc.taskmanager.exception.user;

public class UserIdMismatchException extends RuntimeException {
    public UserIdMismatchException(String message) {
        super(message);
    }

    public UserIdMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
