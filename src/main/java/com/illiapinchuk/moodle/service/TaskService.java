package com.illiapinchuk.moodle.service;

import com.illiapinchuk.moodle.model.dto.TaskDto;
import com.illiapinchuk.moodle.persistence.entity.Task;

/** Service interface for managing tasks. */
public interface TaskService {

  /**
   * Retrieves a task by its ID.
   *
   * @param id The ID of the task to retrieve.
   * @return The task with the specified ID, or null if not found.
   */
  Task getTaskById(String id);

  /**
   * Creates a new task.
   *
   * @param task The task object to create.
   * @return The created task.
   */
  Task createTask(Task task);

  /**
   * Updates an existing task.
   *
   * @param taskDto The task DTO containing the updated task data.
   * @return The updated task.
   */
  Task updateTask(TaskDto taskDto);

  /**
   * Deletes a task by its ID.
   *
   * @param id The ID of the task to delete.
   */
  void deleteTaskById(String id);
}
