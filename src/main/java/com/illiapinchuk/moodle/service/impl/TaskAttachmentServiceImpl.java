package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.moodle.persistence.entity.TaskAttachment;
import com.illiapinchuk.moodle.persistence.repository.TaskAttachmentRepository;
import com.illiapinchuk.moodle.service.TaskAttachmentService;
import jakarta.validation.constraints.NotNull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Implementation of {@link TaskAttachmentService} interface. */
@Service
@RequiredArgsConstructor
public class TaskAttachmentServiceImpl implements TaskAttachmentService {

  private final TaskAttachmentRepository taskAttachmentRepository;

  @Override
  public List<TaskAttachment> getAttachmentsByTaskId(@NotNull final String taskId) {
    return taskAttachmentRepository.getTaskAttachmentsByTaskId(taskId);
  }

  @Override
  public TaskAttachment saveTaskAttachment(@NotNull final TaskAttachment taskAttachment) {
    return taskAttachmentRepository.save(taskAttachment);
  }

  @Override
  public void deleteTaskAttachment(@NotNull final TaskAttachment taskAttachment) {
    taskAttachmentRepository.delete(taskAttachment);
  }
}
