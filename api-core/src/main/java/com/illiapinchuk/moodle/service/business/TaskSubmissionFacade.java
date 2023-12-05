package com.illiapinchuk.moodle.service.business;

import com.illiapinchuk.moodle.model.dto.TaskDto;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface TaskSubmissionFacade {
  TaskDto addSubmissionToTask(String submissionJson, List<MultipartFile> files, String taskId);
}
