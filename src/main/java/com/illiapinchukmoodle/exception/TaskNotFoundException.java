package com.illiapinchukmoodle.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Illia Pinchuk
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TaskNotFoundException extends RuntimeException{
    public TaskNotFoundException(Long taskId) {
        super("Could not find task with id " + taskId);
    }
}
