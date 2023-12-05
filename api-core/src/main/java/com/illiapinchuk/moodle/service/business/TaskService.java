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

  TaskDto addAttachmentToTask(MultipartFile file, String taskId);
}
