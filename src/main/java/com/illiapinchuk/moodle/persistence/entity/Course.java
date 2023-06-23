package com.illiapinchuk.moodle.persistence.entity;

import jakarta.validation.Valid;
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
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

/** Course class represents a Course in the mongo db. */
@Document(collection = "courses")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Course {

  @Id String id;

  @NotBlank(message = "Name is mandatory")
  @Size(min = 1, max = 200, message = "Name should have between 1 and 200 characters")
  String name;

  @NotBlank(message = "Description is mandatory")
  @Size(min = 1, max = 2000, message = "Description should have between 1 and 2000 characters")
  String description;

  @NotEmpty(message = "There must be at least one author")
  List<@NotBlank String> authorIds;

  List<@NotBlank String> students;

  @DBRef
  List<@Valid Task> tasks;
}
