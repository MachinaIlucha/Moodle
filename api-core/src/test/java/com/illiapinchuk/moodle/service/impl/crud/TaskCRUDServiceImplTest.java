package com.illiapinchuk.moodle.service.impl.crud;

import com.illiapinchuk.moodle.common.TestConstants;
import com.illiapinchuk.moodle.common.mapper.TaskMapper;
import com.illiapinchuk.moodle.common.validator.CourseValidator;
import com.illiapinchuk.moodle.common.validator.TaskValidator;
import com.illiapinchuk.moodle.configuration.security.UserPermissionService;
import com.illiapinchuk.moodle.exception.TaskNotFoundException;
import com.illiapinchuk.moodle.persistence.entity.TaskAttachment;
import com.illiapinchuk.moodle.persistence.repository.TaskRepository;
import com.illiapinchuk.moodle.service.business.TaskAttachmentService;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Optional;

import static com.illiapinchuk.moodle.common.TestConstants.TaskConstants.TASK_ID;
import static com.illiapinchuk.moodle.common.TestConstants.TaskConstants.VALID_TASK_1;
import static com.illiapinchuk.moodle.common.TestConstants.TaskConstants.VALID_TASK_DTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskCRUDServiceImplTest {

  @Mock private TaskRepository taskRepository;
  @Mock private TaskMapper taskMapper;
  @Mock private TaskValidator taskValidator;
  @Mock private CourseValidator courseValidator;
  @Mock private TaskAttachmentService taskAttachmentService;
  @InjectMocks private TaskCRUDServiceImpl taskCRUDService;

  private static MockedStatic<UserPermissionService> mockedUserPermissionService;

  @BeforeAll
  static void setupUserPermissionServiceMocks() {
    mockedUserPermissionService = mockStatic(UserPermissionService.class);
    mockedUserPermissionService
        .when(UserPermissionService::getJwtUser)
        .thenReturn(TestConstants.UserConstants.ADMIN_JWT_USER);
    mockedUserPermissionService.when(UserPermissionService::hasAnyRulingRole).thenReturn(true);
  }

  @AfterAll
  static void closeUserPermissionServiceMocks() {
    mockedUserPermissionService.close();
  }

  @Test
  void testGetTaskById_ValidId_TaskFound() {
    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(VALID_TASK_1));
    when(taskAttachmentService.getAttachmentsByTaskId(TASK_ID)).thenReturn(new ArrayList<>());

    final var result = taskCRUDService.getTaskById(TASK_ID);

    assertEquals(VALID_TASK_1, result);
    verify(taskAttachmentService).getAttachmentsByTaskId(TASK_ID);
  }

  @Test
  void testGetTaskById_InvalidId_TaskNotFound() {
    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.empty());

    assertThrows(TaskNotFoundException.class, () -> taskCRUDService.getTaskById(TASK_ID));
  }

  @Test
  void testGetTaskById_UserNotEnrolledWithRulingRole_TaskFound() {
    when(taskRepository.findById(TASK_ID)).thenReturn(Optional.of(VALID_TASK_1));
    when(courseValidator.isStudentEnrolledInCourseWithTask(eq(TASK_ID), any())).thenReturn(false);
    when(taskAttachmentService.getAttachmentsByTaskId(TASK_ID)).thenReturn(new ArrayList<>());

    final var result = taskCRUDService.getTaskById(TASK_ID);

    assertEquals(VALID_TASK_1, result);
    verify(taskAttachmentService).getAttachmentsByTaskId(TASK_ID);
  }

  @Test
  void testCreateTask_ValidAuthorId_TaskCreated() {
    when(taskRepository.save(VALID_TASK_1)).thenReturn(VALID_TASK_1);

    final var result = taskCRUDService.createTask(VALID_TASK_1);

    assertEquals(VALID_TASK_1, result);
    verify(taskRepository).save(VALID_TASK_1);
  }

  @Test
  void testUpdateTask_ValidTaskDto_TaskUpdated() {
    when(taskValidator.isTaskExistsInDbById(VALID_TASK_DTO.getId())).thenReturn(true);
    when(taskRepository.findById(VALID_TASK_DTO.getId())).thenReturn(Optional.of(VALID_TASK_1));
    when(taskRepository.save(VALID_TASK_1)).thenReturn(VALID_TASK_1);

    final var result = taskCRUDService.updateTaskFromDto(VALID_TASK_DTO);

    assertEquals(VALID_TASK_1, result);
    verify(taskValidator).isTaskExistsInDbById(VALID_TASK_DTO.getId());
    verify(taskRepository).findById(VALID_TASK_DTO.getId());
    verify(taskMapper).updateTask(VALID_TASK_1, VALID_TASK_DTO);
    verify(taskRepository).save(VALID_TASK_1);
  }

  @Test
  void testUpdateTask_InvalidTaskDto_TaskNotFound() {
    when(taskValidator.isTaskExistsInDbById(VALID_TASK_DTO.getId())).thenReturn(false);

    assertThrows(
        TaskNotFoundException.class, () -> taskCRUDService.updateTaskFromDto(VALID_TASK_DTO));
    verify(taskValidator).isTaskExistsInDbById(VALID_TASK_DTO.getId());
  }

  @Test
  void testDeleteTaskById_ValidId_TaskDeleted() {
    when(taskAttachmentService.getAttachmentsByTaskId(TASK_ID))
        .thenReturn(TestConstants.TaskAttachmentConstants.LIST_OF_VALID_ATTACHMENTS);

    taskCRUDService.deleteTaskById(TASK_ID);

    verify(taskAttachmentService).getAttachmentsByTaskId(TASK_ID);
    verify(
            taskAttachmentService,
            times(TestConstants.TaskAttachmentConstants.LIST_OF_VALID_ATTACHMENTS.size()))
        .deleteTaskAttachment(any(TaskAttachment.class));
    verify(taskRepository).deleteById(TASK_ID);
  }
}
