package com.illiapinchukmoodle.data.dto;

import com.illiapinchukmoodle.data.model.Status;
import lombok.Data;

import java.util.Date;

/**
 * Incoming DTO to create a new record of {@link com.illiapinchukmoodle.data.model.Task}
 *
 * @author Illia Pinchuk
 */
@Data
public class AdminTaskDTO {
    private Long id;
    private String name;
    private String description;
    private Long courseId;
    private Date created;
    private Date updated;
    private Status status;
}
