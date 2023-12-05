package com.illiapinchuk.moodle.service.impl.business;

import com.illiapinchuk.moodle.persistence.entity.TaskAttachment;
import com.illiapinchuk.moodle.persistence.repository.TaskAttachmentRepository;
import com.illiapinchuk.moodle.service.business.TaskAttachmentService;
import jakarta.annotation.Nonnull;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Implementation of {@link TaskAttachmentService} interface. */
@Service
@RequiredArgsConstructor
public class TaskAttachmentServiceImpl implements TaskAttachmentService {

  private final TaskAttachmentRepository taskAttachmentRepository;

  @Override
  public List<TaskAttachment> getAttachmentsByTaskId(@Nonnull final String taskId) {
    return taskAttachmentRepository.getTaskAttachmentsByTaskId(taskId);
  }

  @Override
  public TaskAttachment saveTaskAttachment(@Nonnull final TaskAttachment taskAttachment) {
    return taskAttachmentRepository.save(taskAttachment);
  }

  @Override
  public void deleteTaskAttachment(@Nonnull final TaskAttachment taskAttachment) {
    taskAttachmentRepository.delete(taskAttachment);
  }
}
