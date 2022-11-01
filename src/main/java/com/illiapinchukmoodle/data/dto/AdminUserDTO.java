package com.illiapinchukmoodle.data.dto;

import com.illiapinchukmoodle.data.model.Status;
import lombok.Data;

import java.util.Date;

/**
 * Incoming DTO to create a new record of {@link com.illiapinchukmoodle.data.model.User}
 *
 * @author Illia Pinchuk
 */
@Data
public class AdminUserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private Date created;
    private Date updated;
    private Status status;
}
