package com.benjamerc.taskmanager.exception.task;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class TaskIdMismatchException extends RuntimeException {
    public TaskIdMismatchException(String message) {
        super(message);
    }

    public TaskIdMismatchException(String message, Throwable cause) {
        super(message, cause);
    }
}
