package com.illiapinchuk.moodle.service.impl.business;

import com.illiapinchuk.moodle.common.TestConstants;
import com.illiapinchuk.moodle.common.validator.TaskValidator;
import com.illiapinchuk.moodle.common.validator.UserValidator;
import com.illiapinchuk.moodle.configuration.security.UserPermissionService;
import com.illiapinchuk.moodle.exception.TaskNotFoundException;
import com.illiapinchuk.moodle.exception.UserDontHaveAccessToResource;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.persistence.entity.Submission;
import com.illiapinchuk.moodle.persistence.repository.SubmissionRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static com.illiapinchuk.moodle.common.TestConstants.TaskConstants.TASK_ID;
import static com.illiapinchuk.moodle.common.TestConstants.UserConstants.USER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SubmissionServiceImplTest {

  @Mock private SubmissionRepository submissionRepository;

  @Mock private UserValidator userValidator;

  @Mock private TaskValidator taskValidator;

  @InjectMocks private SubmissionServiceImpl submissionService;

  @Test
  void getSubmissionsByTaskIdAndStudentId_Success() {
    try (MockedStatic<UserPermissionService> mocked = mockStatic(UserPermissionService.class)) {
      mocked.when(UserPermissionService::hasTeacherRole).thenReturn(true);

      final var taskId = TASK_ID;
      final var studentId = USER_ID;
      final var expectedSubmissions = List.of(new Submission());

      when(taskValidator.isTaskExistsInDbById(taskId)).thenReturn(true);
      when(userValidator.isUserExistInDbById(studentId)).thenReturn(true);
      when(submissionRepository.getSubmissionsByTaskIdAndAndUserId(taskId, studentId))
          .thenReturn(expectedSubmissions);
      when(taskValidator.isTeacherCanModifyTask(taskId)).thenReturn(true);

      final var actualSubmissions =
          submissionService.getSubmissionsByTaskIdAndStudentId(taskId, studentId);
      assertEquals(expectedSubmissions, actualSubmissions);
    }
  }

  @Test
  void getSubmissionsByTaskIdAndStudentId_TaskNotFound() {
    final var taskId = TASK_ID;
    final var studentId = USER_ID;

    when(taskValidator.isTaskExistsInDbById(taskId)).thenReturn(false);

    assertThrows(
        TaskNotFoundException.class,
        () -> {
          submissionService.getSubmissionsByTaskIdAndStudentId(taskId, studentId);
        });
  }

  @Test
  void getSubmissionsByTaskIdAndStudentId_UserNotFound() {
    try (MockedStatic<UserPermissionService> mocked = mockStatic(UserPermissionService.class)) {
      mocked.when(UserPermissionService::hasTeacherRole).thenReturn(false);

      final var taskId = TASK_ID;
      final var studentId = USER_ID;

      when(taskValidator.isTaskExistsInDbById(taskId)).thenReturn(true);
      when(userValidator.isUserExistInDbById(studentId)).thenReturn(false);

      assertThrows(
          UserNotFoundException.class,
          () -> {
            submissionService.getSubmissionsByTaskIdAndStudentId(taskId, studentId);
          });
    }
  }

  @Test
  void getSubmissionsByTaskIdAndStudentId_AccessDenied() {
    try (MockedStatic<UserPermissionService> mocked = mockStatic(UserPermissionService.class)) {
      mocked.when(UserPermissionService::hasTeacherRole).thenReturn(false);
      mocked.when(UserPermissionService::hasUserRole).thenReturn(true);

      final var taskId = TASK_ID;
      final var studentId = USER_ID;

      when(taskValidator.isTaskExistsInDbById(taskId)).thenReturn(true);
      when(userValidator.isUserExistInDbById(studentId)).thenReturn(true);
      when(taskValidator.isStudentHaveAccessToTask(taskId)).thenReturn(false);

      assertThrows(
          UserDontHaveAccessToResource.class,
          () -> {
            submissionService.getSubmissionsByTaskIdAndStudentId(taskId, studentId);
          });
    }
  }
}
