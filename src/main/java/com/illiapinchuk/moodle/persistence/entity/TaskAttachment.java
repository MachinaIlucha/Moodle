package com.illiapinchuk.moodle.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.mongodb.core.mapping.Document;

/** Task class represents a Task in the mongo db. */
@Document(collection = "tasks-files")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TaskAttachment {

  @JsonIgnore
  String taskId;

  String fileName;
}
