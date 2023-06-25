package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.moodle.persistence.entity.Task;
import com.illiapinchuk.moodle.persistence.entity.TaskAttachment;
import com.illiapinchuk.moodle.persistence.repository.TaskAttachmentRepository;
import com.illiapinchuk.moodle.service.TaskAttachmentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/** Implementation of {@link TaskAttachmentService} interface. */
@Service
@RequiredArgsConstructor
public class TaskAttachmentServiceImpl implements TaskAttachmentService {

  private final TaskAttachmentRepository taskAttachmentRepository;

  @Override
  public List<TaskAttachment> getAttachmentsByTask(Task task) {
    return taskAttachmentRepository.getTaskAttachmentsByTaskId(task.getId());
  }

  @Override
  public TaskAttachment saveTaskAttachment(TaskAttachment taskAttachment) {
    return taskAttachmentRepository.save(taskAttachment);
  }

  @Override
  public void deleteTaskAttachment(TaskAttachment taskAttachment) {
    taskAttachmentRepository.delete(taskAttachment);
  }
}
