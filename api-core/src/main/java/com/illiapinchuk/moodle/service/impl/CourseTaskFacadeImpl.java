package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.moodle.common.mapper.CourseMapper;
import com.illiapinchuk.moodle.common.mapper.TaskMapper;
import com.illiapinchuk.moodle.common.validator.UserValidator;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.model.dto.CourseDto;
import com.illiapinchuk.moodle.model.dto.TaskDto;
import com.illiapinchuk.moodle.persistence.entity.Submission;
import com.illiapinchuk.moodle.persistence.entity.Task;
import com.illiapinchuk.moodle.service.CourseService;
import com.illiapinchuk.moodle.service.CourseTaskFacade;
import com.illiapinchuk.moodle.service.SubmissionService;
import com.illiapinchuk.moodle.service.TaskService;
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

  private final TaskService taskService;
  private final CourseService courseService;
  private final SubmissionService submissionService;

  private final UserValidator userValidator;
  private final TaskMapper taskMapper;
  private final CourseMapper courseMapper;

  @Override
  public CourseDto getCourseWithTasks(@Nonnull final String courseId) {
    final var course = courseService.getCourseById(courseId);

    return courseMapper.courseToCourseDto(course);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public void deleteCourseByIdWithTasks(@Nonnull final String courseId) {
    final var course = courseService.getCourseById(courseId);

    course.getTasks().forEach(task -> taskService.deleteTaskById(task.getId()));

    courseService.deleteCourseById(courseId);
  }

  @Override
  @Transactional
  public TaskDto createTaskWithCourse(@Nonnull final TaskDto taskDto) {
    final var task = taskMapper.taskDtoToTask(taskDto);

    final var courseId = taskDto.getCourseId();
    final var course = courseService.getCourseById(courseId);

    task.setCourse(course);

    // Retrieve the author ID from the task
    final var authorId = task.getAuthorId();

    // Check if the author exists in the database
    if (!userValidator.isUserExistInDbById(authorId)) {
      throw new UserNotFoundException(
          String.format("The author with id: %s does not exist.", authorId));
    }

    task.setCreationDate(LocalDateTime.now());

    final var createdTask = taskService.createTask(task);
    addTaskToCourse(courseId, createdTask.getId());
    return taskMapper.taskToTaskDto(createdTask);
  }

  @Override
  public void addTaskToCourse(@Nonnull final String courseId, @Nonnull final String taskId) {
    final var course = courseService.getCourseById(courseId);
    final var task = taskService.getTaskById(taskId);

    course.getTasks().add(task);

    courseService.updateCourse(course);
  }

  @Override
  public Integer getOverallGradeForStudentInCourse(
      @Nonnull final String courseId, @Nonnull final Long studentId) {
    // Check if the student exists in the database
    if (!userValidator.isUserExistInDbById(studentId)) {
      throw new UserNotFoundException(
              String.format("The student with id: %s does not exist.", studentId));
    }

    final var course = courseService.getCourseById(courseId);
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
        .map(submissionService::getSubmissionById)
        .filter(submission -> submission.getUserId().equals(studentId))
        .max(Comparator.comparingInt(submission -> submission.getGrade().getScore()))
        .orElse(null);
  }
}
