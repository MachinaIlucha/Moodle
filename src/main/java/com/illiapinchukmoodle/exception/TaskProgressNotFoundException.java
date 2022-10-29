package com.illiapinchukmoodle.exception;

public class TaskProgressNotFoundException extends RuntimeException {
    public TaskProgressNotFoundException(Long taskProgressId) {
        super("Could not find task progress with id " + taskProgressId);
    }
}
