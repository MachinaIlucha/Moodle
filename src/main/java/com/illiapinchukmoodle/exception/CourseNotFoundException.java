package com.illiapinchukmoodle.exception;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(Long courseId) {
        super("Could not find course with id " + courseId);
    }
}
