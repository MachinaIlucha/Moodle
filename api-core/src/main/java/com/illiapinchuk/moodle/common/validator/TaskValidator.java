package com.illiapinchuk.moodle.common.validator;

import com.illiapinchuk.moodle.persistence.repository.TaskRepository;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** Validation for task-related information. */
@Component
@RequiredArgsConstructor
public class TaskValidator {

  private final TaskRepository taskRepository;

  /**
   * Check if the given task exists in the database.
   *
   * @param id the id to check
   * @return true if the task with this id exists in the database, false otherwise
   */
  public boolean isTaskExistsInDbById(@Nonnull final String id) {
    return taskRepository.existsById(id);
  }
}
