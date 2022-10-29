package com.illiapinchukmoodle.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * Incoming DTO to create a new record of {@link com.illiapinchukmoodle.data.model.Task}
 *
 * @author Illia Pinchuk
 */
@Data
public class TaskDTO {
    @JsonProperty(required = true)
    @NotEmpty
    @NotBlank
    private Long id;

    @JsonProperty(required = true)
    @NotEmpty
    @NotBlank
    private String name;

    @JsonProperty(required = true)
    private String description;

    @JsonProperty(required = true)
    private Long courseId;
}
