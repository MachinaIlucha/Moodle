package com.illiapinchuk.moodle.service.business;

import com.illiapinchuk.moodle.model.dto.TaskDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/** Facade for task submission operations. */
public interface TaskSubmissionFacade {

  /**
   * Adds a submission to a task.
   *
   * @param submissionJson the submission in JSON format
   * @param files the files to be submitted
   * @param taskId the id of the task to which the submission is to be added
   * @return a {@link TaskDto} object
   */
  TaskDto addSubmissionToTask(String submissionJson, List<MultipartFile> files, String taskId);
}
