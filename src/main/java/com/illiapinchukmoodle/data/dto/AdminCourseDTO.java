package com.illiapinchukmoodle.data.dto;

import com.illiapinchukmoodle.data.model.Status;
import lombok.Data;

import java.util.Date;

/**
 * Incoming DTO to create a new record of {@link com.illiapinchukmoodle.data.model.Course}
 *
 * @author Illia Pinchuk
 */
@Data
public class AdminCourseDTO {
    private Long id;
    private String name;
    private String description;
    private Date created;
    private Date updated;
    private Status status;
}
