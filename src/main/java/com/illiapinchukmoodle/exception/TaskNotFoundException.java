package com.illiapinchukmoodle.exception;

public class TaskNotFoundException extends RuntimeException{
    public TaskNotFoundException(Long taskId) {
        super("Could not find task with id " + taskId);
    }
}
