package com.illiapinchukmoodle.data.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CourseDTO {
    @JsonProperty(required = true)
    @NotEmpty
    @NotBlank
    private Long id;

    @JsonProperty(required = true)
    @NotEmpty
    @NotBlank
    private String name;

    @JsonProperty(required = true)
    @Size(max = 255)
    private String description;
}
