package com.illiapinchukmoodle.data.dto;

import lombok.Data;

/**
 * Incoming DTO to login user
 *
 * @author Illia Pinchuk
 */
@Data
public class AuthenticationRequestDto {
    private String email;
    private String password;
}
