package com.illiapinchukmoodle.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;


/**
 * Incoming DTO to create a new record of {@link com.illiapinchukmoodle.data.model.TaskProgress}
 *
 * @author Illia Pinchuk
 */
@Data
public class TaskProgressDTO {

    @JsonProperty(required = true)
    @NotEmpty
    @NotBlank
    private Long userId;

    @JsonProperty(required = true)
    @NotEmpty
    @NotBlank
    private Long taskId;

    @JsonProperty(required = true)
    @Size(max = 255)
    private String attachments;
    private int score;
    private boolean verified;
}
