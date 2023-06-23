package com.illiapinchuk.moodle.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.illiapinchuk.moodle.persistence.entity.Course;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/** Incoming DTO to represent {@link Course}. */
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {

  @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
  private String id;

  @NotBlank(message = "Name is mandatory")
  @Size(min = 1, max = 200, message = "Name should have between 1 and 200 characters")
  String name;

  @NotBlank(message = "Description is mandatory")
  @Size(min = 1, max = 2000, message = "Description should have between 1 and 2000 characters")
  String description;

  @NotEmpty(message = "There must be at least one author")
  List<@NotBlank String> authorIds;

  List<@NotBlank String> students;

  @JsonProperty(access = JsonProperty.Access.READ_ONLY)
  List<@NotBlank String> tasks;
}
