package com.illiapinchuk.moodle.service.impl.business;

import com.illiapinchuk.moodle.common.mapper.CourseMapper;
import com.illiapinchuk.moodle.common.mapper.TaskMapper;
import com.illiapinchuk.moodle.common.validator.CourseValidator;
import com.illiapinchuk.moodle.common.validator.UserValidator;
import com.illiapinchuk.moodle.configuration.security.UserPermissionService;
import com.illiapinchuk.moodle.exception.UserDontHaveAccessToResource;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.model.dto.CourseDto;
import com.illiapinchuk.moodle.model.dto.TaskDto;
import com.illiapinchuk.moodle.persistence.entity.Submission;
import com.illiapinchuk.moodle.persistence.entity.Task;
import com.illiapinchuk.moodle.service.business.CourseTaskFacade;
import com.illiapinchuk.moodle.service.crud.CourseCRUDService;
import com.illiapinchuk.moodle.service.crud.SubmissionCRUDService;
import com.illiapinchuk.moodle.service.crud.TaskCRUDService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.Objects;

/** Implementation of {@link CourseTaskFacade} interface. */
@Service
@RequiredArgsConstructor
public class CourseTaskFacadeImpl implements CourseTaskFacade {

  private final TaskCRUDService taskCRUDService;
  private final CourseCRUDService courseCRUDService;
  private final SubmissionCRUDService submissionCRUDService;

  private final UserValidator userValidator;
  private final CourseValidator courseValidator;

  private final TaskMapper taskMapper;
  private final CourseMapper courseMapper;

  @Override
  public CourseDto getCourseWithTasks(@Nonnull final String courseId) {
    final var course = courseCRUDService.getCourseById(courseId);

    return courseMapper.courseToCourseDto(course);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteCourseByIdWithTasks(@Nonnull final String courseId) {
    final var course = courseCRUDService.getCourseById(courseId);

    course.getTasks().forEach(task -> taskCRUDService.deleteTaskById(task.getId()));

    courseCRUDService.deleteCourseById(courseId);
  }

  @Override
  @Transactional
  public TaskDto createTaskWithCourse(@Nonnull final TaskDto taskDto) {
    final var task = taskMapper.taskDtoToTask(taskDto);

    final var courseId = taskDto.getCourseId();
    final var course = courseCRUDService.getCourseById(courseId);

    if (UserPermissionService.hasTeacherRole()
        && !courseValidator.isTeacherCanModifyCourse(courseId)) {
      throw new UserDontHaveAccessToResource("User does not have access to this resource.");
    }

    task.setCourse(course);

    // Retrieve the author ID from the task
    final var authorId = task.getAuthorId();

    // Check if the author exists in the database
    if (!userValidator.isUserExistInDbById(authorId)) {
      throw new UserNotFoundException(
          String.format("The author with id: %s does not exist.", authorId));
    }

    task.setCreationDate(LocalDateTime.now());

    final var createdTask = taskCRUDService.createTask(task);
    addTaskToCourse(courseId, createdTask.getId());
    return taskMapper.taskToTaskDto(createdTask);
  }

  @Override
  public void addTaskToCourse(@Nonnull final String courseId, @Nonnull final String taskId) {
    final var course = courseCRUDService.getCourseById(courseId);
    final var task = taskCRUDService.getTaskById(taskId);

    course.getTasks().add(task);

    courseCRUDService.updateCourse(course);
  }

  @Override
  public Integer getOverallGradeForStudentInCourse(
      @Nonnull final String courseId, @Nonnull final Long studentId) {
    // Check if the student exists in the database
    if (!userValidator.isUserExistInDbById(studentId)) {
      throw new UserNotFoundException(
          String.format("The student with id: %s does not exist.", studentId));
    }

    final var course = courseCRUDService.getCourseById(courseId);

    if (UserPermissionService.hasUserRole()
        && !courseValidator.isStudentHasAccessToCourse(courseId)) {
      throw new UserDontHaveAccessToResource("User does not have access to this resource.");
    }

    if (UserPermissionService.hasTeacherRole()
        && !courseValidator.isTeacherCanModifyCourse(courseId)) {
      throw new UserDontHaveAccessToResource("User does not have access to this resource.");
    }

    final var tasks = course.getTasks();

    final var bestSubmissions =
        tasks.stream()
            .map(task -> getBestSubmissionForTask(task, studentId))
            .filter(Objects::nonNull)
            .toList();

    return !bestSubmissions.isEmpty()
        ? bestSubmissions.stream()
            .map(submission -> submission.getGrade().getScore())
            .reduce(0, Integer::sum)
        : 0;
  }

  private Submission getBestSubmissionForTask(
      @Nonnull final Task task, @Nonnull final Long studentId) {
    return task.getSubmissionIds().stream()
        .map(submissionCRUDService::getSubmissionById)
        .filter(submission -> submission.getUserId().equals(studentId))
        .max(Comparator.comparingInt(submission -> submission.getGrade().getScore()))
        .orElse(null);
  }
}
