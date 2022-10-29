package com.illiapinchukmoodle.exception;

public class UserNotFoundException extends RuntimeException{

    public UserNotFoundException(Long userId) {
        super("Could not find user with id " + userId);
    }
}
