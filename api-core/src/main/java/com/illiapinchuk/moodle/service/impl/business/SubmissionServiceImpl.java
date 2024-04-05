package com.illiapinchuk.moodle.service.impl.business;

import com.illiapinchuk.moodle.common.validator.TaskValidator;
import com.illiapinchuk.moodle.common.validator.UserValidator;
import com.illiapinchuk.moodle.configuration.security.UserPermissionService;
import com.illiapinchuk.moodle.exception.TaskNotFoundException;
import com.illiapinchuk.moodle.exception.UserDontHaveAccessToResource;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.persistence.entity.Submission;
import com.illiapinchuk.moodle.persistence.repository.SubmissionRepository;
import com.illiapinchuk.moodle.service.business.SubmissionService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

/** Implementation of {@link SubmissionService} interface. */
@Service
@RequiredArgsConstructor
public class SubmissionServiceImpl implements SubmissionService {

  private final SubmissionRepository submissionRepository;

  private final UserValidator userValidator;
  private final TaskValidator taskValidator;

  @Override
  @Transactional(readOnly = true)
  public List<Submission> getSubmissionsByTaskIdAndStudentId(
      @Nonnull final String taskId, @Nonnull final Long studentId) {
    if (!taskValidator.isTaskExistsInDbById(taskId)) {
      throw new TaskNotFoundException("Task with id: " + taskId + " does not exist.");
    }
    if (!userValidator.isUserExistInDbById(studentId)) {
      throw new UserNotFoundException("User with id: " + studentId + " does not exist.");
    }

    if (UserPermissionService.hasTeacherRole() && !taskValidator.isTeacherCanModifyTask(taskId)) {
      throw new UserDontHaveAccessToResource("Teacher does not have access to this resource.");
    }

    if (UserPermissionService.hasUserRole() && !taskValidator.isStudentHaveAccessToTask(taskId)) {
      throw new UserDontHaveAccessToResource("User does not have access to this resource.");
    }

    return submissionRepository.getSubmissionsByTaskIdAndAndUserId(taskId, studentId);
  }

  @Override
  @Transactional(readOnly = true)
  public List<Submission> getAllSubmissionsForTask(@Nonnull final String taskId) {
    if (!taskValidator.isTaskExistsInDbById(taskId)) {
      throw new TaskNotFoundException("Task with id: " + taskId + " does not exist.");
    }

    if (UserPermissionService.hasTeacherRole() && !taskValidator.isTeacherCanModifyTask(taskId)) {
      throw new UserDontHaveAccessToResource("User does not have access to this resource.");
    }

    return submissionRepository.getSubmissionsByTaskId(taskId);
  }
}
