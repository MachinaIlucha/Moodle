package com.illiapinchukmoodle.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/**
 * Incoming DTO to create a new record of {@link com.illiapinchukmoodle.data.model.User}
 *
 * @author Illia Pinchuk
 */
@Data
public class UserDTO {
    @JsonProperty(required = true)
    @NotEmpty
    @NotBlank
    private Long id;

    @JsonProperty(required = true)
    @NotEmpty
    @NotBlank
    private String firstName;

    @JsonProperty(required = true)
    @NotEmpty
    @NotBlank
    private String lastName;

    @JsonProperty(required = true)
    @Email(message = "User email should be valid")
    private String email;

    @JsonProperty(required = true)
    @Size(min = 6, max = 20, message = "Password should be greater then equal to 6 and less than 20 characters")
    private String password;
}
