package com.illiapinchukmoodle.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Illia Pinchuk
 */
@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TaskProgressNotFoundException extends RuntimeException {
    public TaskProgressNotFoundException(Long taskProgressId) {
        super("Could not find task progress with id " + taskProgressId);
    }
}
