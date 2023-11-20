package com.illiapinchuk.moodle.service;

import com.illiapinchuk.moodle.model.dto.TaskDto;
import com.illiapinchuk.moodle.persistence.entity.Task;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

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

  /**
   * Retrieves a list of tasks associated with the given course ID.
   *
   * @param courseId the unique identifier of the course
   * @return a list of tasks related to the specified course ID
   */
  List<Task> getTasksByCourseId(String courseId);

  TaskDto addAttachmentToTask(MultipartFile file, String taskId);

  TaskDto addSubmissionToTask(String submissionJson, List<MultipartFile> files, String taskId);
}
