package com.illiapinchuk.moodle.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.illiapinchuk.moodle.common.TestConstants;
import com.illiapinchuk.moodle.persistence.repository.TaskAttachmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskAttachmentServiceImplTest {

  @Mock private TaskAttachmentRepository taskAttachmentRepository;
  @InjectMocks private TaskAttachmentServiceImpl taskAttachmentService;

  @Test
  void getAttachmentsByTaskId_ShouldReturnListOfAttachments() {
    when(taskAttachmentRepository.getTaskAttachmentsByTaskId(TestConstants.TaskConstants.TASK_ID))
        .thenReturn(TestConstants.TaskAttachmentConstants.LIST_OF_VALID_ATTACHMENTS);

    final var actualAttachments =
        taskAttachmentService.getAttachmentsByTaskId(TestConstants.TaskConstants.TASK_ID);

    assertEquals(
        TestConstants.TaskAttachmentConstants.LIST_OF_VALID_ATTACHMENTS, actualAttachments);
    verify(taskAttachmentRepository)
        .getTaskAttachmentsByTaskId(TestConstants.TaskConstants.TASK_ID);
  }

  @Test
  void saveTaskAttachment_ShouldReturnSavedAttachment() {
    when(taskAttachmentRepository.save(TestConstants.TaskAttachmentConstants.VALID_ATTACHMENT))
        .thenReturn(TestConstants.TaskAttachmentConstants.VALID_ATTACHMENT);

    final var savedAttachment =
        taskAttachmentService.saveTaskAttachment(
            TestConstants.TaskAttachmentConstants.VALID_ATTACHMENT);

    assertEquals(TestConstants.TaskAttachmentConstants.VALID_ATTACHMENT, savedAttachment);
    verify(taskAttachmentRepository).save(TestConstants.TaskAttachmentConstants.VALID_ATTACHMENT);
  }

  @Test
  void deleteTaskAttachment_ShouldCallRepositoryDelete() {
    taskAttachmentService.deleteTaskAttachment(
        TestConstants.TaskAttachmentConstants.VALID_ATTACHMENT);

    verify(taskAttachmentRepository).delete(TestConstants.TaskAttachmentConstants.VALID_ATTACHMENT);
  }
}
