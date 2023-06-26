package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.moodle.persistence.entity.TaskAttachment;
import com.illiapinchuk.moodle.persistence.repository.TaskAttachmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.List;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskAttachmentServiceImplTest {

  private static final String TASK_ID = "task1";

  @Mock private TaskAttachmentRepository taskAttachmentRepository;

  @InjectMocks private TaskAttachmentServiceImpl taskAttachmentService;

  @Test
  void getAttachmentsByTaskId_ShouldReturnAttachments() {
    List<TaskAttachment> expectedAttachments = new ArrayList<>();
    expectedAttachments.add(new TaskAttachment());
    when(taskAttachmentRepository.getTaskAttachmentsByTaskId(TASK_ID))
        .thenReturn(expectedAttachments);

    List<TaskAttachment> attachments = taskAttachmentService.getAttachmentsByTaskId(TASK_ID);

    assertEquals(expectedAttachments, attachments);
    verify(taskAttachmentRepository, times(1)).getTaskAttachmentsByTaskId(TASK_ID);
  }

  @Test
  void saveTaskAttachment_ShouldReturnSavedAttachment() {
    TaskAttachment taskAttachment = new TaskAttachment();
    when(taskAttachmentRepository.save(taskAttachment)).thenReturn(taskAttachment);

    TaskAttachment savedAttachment = taskAttachmentService.saveTaskAttachment(taskAttachment);

    assertEquals(taskAttachment, savedAttachment);
    verify(taskAttachmentRepository, times(1)).save(taskAttachment);
  }

  @Test
  void deleteTaskAttachment_ShouldDeleteAttachment() {
    TaskAttachment taskAttachment = new TaskAttachment();

    taskAttachmentService.deleteTaskAttachment(taskAttachment);

    verify(taskAttachmentRepository, times(1)).delete(taskAttachment);
  }
}
