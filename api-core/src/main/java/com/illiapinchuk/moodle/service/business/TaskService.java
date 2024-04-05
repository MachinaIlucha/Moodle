package com.illiapinchuk.moodle.service.business;

import com.illiapinchuk.moodle.model.dto.TaskDto;
import com.illiapinchuk.moodle.persistence.entity.Task;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/** Service interface for managing tasks. */
public interface TaskService {

  /**
   * Retrieves a list of tasks associated with the given course ID.
   *
   * @param courseId the unique identifier of the course
   * @return a list of tasks related to the specified course ID
   */
  List<Task> getTasksByCourseId(String courseId);

  /**
   * Adds an attachment to the task identified by the given task ID.
   *
   * @param file The file to be attached to the task. This is the attachment that will be associated
   *     with the task.
   * @param taskId The unique identifier of the task to which the file will be attached.
   * @return A {@link TaskDto} object representing the task with the newly added attachment.
   */
  TaskDto addAttachmentToTask(MultipartFile file, String taskId);
}
