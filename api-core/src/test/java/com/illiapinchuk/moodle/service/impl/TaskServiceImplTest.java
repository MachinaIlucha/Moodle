package com.illiapinchuk.moodle.service.impl;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.illiapinchuk.moodle.common.TestConstants;
import com.illiapinchuk.moodle.common.date.DateService;
import com.illiapinchuk.moodle.common.mapper.TaskMapper;
import com.illiapinchuk.moodle.common.validator.TaskValidator;
import com.illiapinchuk.moodle.common.validator.UserValidator;
import com.illiapinchuk.moodle.exception.TaskNotFoundException;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.persistence.entity.TaskAttachment;
import com.illiapinchuk.moodle.persistence.repository.TaskRepository;
import com.illiapinchuk.moodle.service.TaskAttachmentService;
import java.util.ArrayList;
import java.util.Date;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

  @Mock private TaskRepository taskRepository;
  @Mock private TaskMapper taskMapper;
  @Mock private TaskValidator taskValidator;
  @Mock private DateService dateService;
  @Mock private UserValidator userValidator;
  @Mock private TaskAttachmentService taskAttachmentService;
  @InjectMocks private TaskServiceImpl taskService;

  @Test
  void testGetTaskById_ValidId_TaskFound() {
    when(taskRepository.findById(TestConstants.TaskConstants.TASK_ID))
        .thenReturn(Optional.of(TestConstants.TaskConstants.VALID_TASK_1));
    when(taskAttachmentService.getAttachmentsByTaskId(TestConstants.TaskConstants.TASK_ID))
        .thenReturn(new ArrayList<>());

    final var result = taskService.getTaskById(TestConstants.TaskConstants.TASK_ID);

    assertEquals(TestConstants.TaskConstants.VALID_TASK_1, result);
    verify(taskAttachmentService).getAttachmentsByTaskId(TestConstants.TaskConstants.TASK_ID);
  }

  @Test
  void testGetTaskById_InvalidId_TaskNotFound() {
    when(taskRepository.findById(TestConstants.TaskConstants.TASK_ID)).thenReturn(Optional.empty());

    assertThrows(
        TaskNotFoundException.class,
        () -> taskService.getTaskById(TestConstants.TaskConstants.TASK_ID));
  }

  @Test
  void testCreateTask_ValidAuthorId_TaskCreated() {
    final var currentDate = new Date();
    when(userValidator.isUserExistInDbById(TestConstants.TaskConstants.VALID_TASK_1.getAuthorId()))
        .thenReturn(true);
    when(dateService.getCurrentZonedDateTime(anyString())).thenReturn(currentDate);
    when(taskRepository.save(TestConstants.TaskConstants.VALID_TASK_1))
        .thenReturn(TestConstants.TaskConstants.VALID_TASK_1);

    final var result = taskService.createTask(TestConstants.TaskConstants.VALID_TASK_1);

    assertEquals(TestConstants.TaskConstants.VALID_TASK_1, result);
    assertEquals(currentDate, TestConstants.TaskConstants.VALID_TASK_1.getCreationDate());
    verify(userValidator)
        .isUserExistInDbById(TestConstants.TaskConstants.VALID_TASK_1.getAuthorId());
    verify(dateService).getCurrentZonedDateTime(anyString());
    verify(taskRepository).save(TestConstants.TaskConstants.VALID_TASK_1);
  }

  @Test
  void testCreateTask_InvalidAuthorId_UserNotFound() {
    when(userValidator.isUserExistInDbById(TestConstants.TaskConstants.VALID_TASK_1.getAuthorId()))
        .thenReturn(false);

    assertThrows(
        UserNotFoundException.class,
        () -> taskService.createTask(TestConstants.TaskConstants.VALID_TASK_1));
    verify(userValidator)
        .isUserExistInDbById(TestConstants.TaskConstants.VALID_TASK_1.getAuthorId());
    verifyNoInteractions(dateService);
    verifyNoInteractions(taskRepository);
  }

  @Test
  void testUpdateTask_ValidTaskDto_TaskUpdated() {
    when(taskValidator.isTaskExistsInDbById(TestConstants.TaskConstants.VALID_TASK_DTO.getId()))
        .thenReturn(true);
    when(taskRepository.findById(TestConstants.TaskConstants.VALID_TASK_DTO.getId()))
        .thenReturn(Optional.of(TestConstants.TaskConstants.VALID_TASK_1));
    when(taskRepository.save(TestConstants.TaskConstants.VALID_TASK_1))
        .thenReturn(TestConstants.TaskConstants.VALID_TASK_1);

    final var result = taskService.updateTask(TestConstants.TaskConstants.VALID_TASK_DTO);

    assertEquals(TestConstants.TaskConstants.VALID_TASK_1, result);
    verify(taskValidator).isTaskExistsInDbById(TestConstants.TaskConstants.VALID_TASK_DTO.getId());
    verify(taskRepository).findById(TestConstants.TaskConstants.VALID_TASK_DTO.getId());
    verify(taskMapper)
        .updateTask(
            TestConstants.TaskConstants.VALID_TASK_1, TestConstants.TaskConstants.VALID_TASK_DTO);
    verify(taskRepository).save(TestConstants.TaskConstants.VALID_TASK_1);
  }

  @Test
  void testUpdateTask_InvalidTaskDto_TaskNotFound() {
    when(taskValidator.isTaskExistsInDbById(TestConstants.TaskConstants.VALID_TASK_DTO.getId()))
        .thenReturn(false);

    assertThrows(
        TaskNotFoundException.class,
        () -> taskService.updateTask(TestConstants.TaskConstants.VALID_TASK_DTO));
    verify(taskValidator).isTaskExistsInDbById(TestConstants.TaskConstants.VALID_TASK_DTO.getId());
  }

  @Test
  void testDeleteTaskById_ValidId_TaskDeleted() {
    when(taskAttachmentService.getAttachmentsByTaskId(TestConstants.TaskConstants.TASK_ID))
        .thenReturn(TestConstants.TaskAttachmentConstants.LIST_OF_VALID_ATTACHMENTS);

    taskService.deleteTaskById(TestConstants.TaskConstants.TASK_ID);

    verify(taskAttachmentService).getAttachmentsByTaskId(TestConstants.TaskConstants.TASK_ID);
    verify(
            taskAttachmentService,
            times(TestConstants.TaskAttachmentConstants.LIST_OF_VALID_ATTACHMENTS.size()))
        .deleteTaskAttachment(any(TaskAttachment.class));
    verify(taskRepository).deleteById(TestConstants.TaskConstants.TASK_ID);
  }

  @Test
  void testGetTasksByCourseId_ValidCourseId_TasksFound() {
    when(taskRepository.getTasksByCourseId(TestConstants.CourseConstants.VALID_COURSE_ID))
        .thenReturn(TestConstants.TaskConstants.LIST_OF_VALID_TASKS);

    final var result =
        taskService.getTasksByCourseId(TestConstants.CourseConstants.VALID_COURSE_ID);

    assertEquals(TestConstants.TaskConstants.LIST_OF_VALID_TASKS, result);
    verify(taskRepository).getTasksByCourseId(TestConstants.CourseConstants.VALID_COURSE_ID);
  }
}
