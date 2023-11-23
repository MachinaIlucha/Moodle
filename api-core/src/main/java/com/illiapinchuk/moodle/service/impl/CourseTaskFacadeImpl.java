package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.moodle.common.ApplicationConstants;
import com.illiapinchuk.moodle.common.date.DateService;
import com.illiapinchuk.moodle.common.mapper.CourseMapper;
import com.illiapinchuk.moodle.common.mapper.TaskMapper;
import com.illiapinchuk.moodle.common.validator.UserValidator;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.model.dto.CourseDto;
import com.illiapinchuk.moodle.model.dto.TaskDto;
import com.illiapinchuk.moodle.service.CourseService;
import com.illiapinchuk.moodle.service.CourseTaskFacade;
import com.illiapinchuk.moodle.service.TaskService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/** Implementation of {@link CourseTaskFacade} interface. */
@Service
@RequiredArgsConstructor
public class CourseTaskFacadeImpl implements CourseTaskFacade {

  private final TaskService taskService;
  private final CourseService courseService;

  private final UserValidator userValidator;
  private final DateService dateService;
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

    // Set the creation date using the date service
    final var creationDate =
        dateService.getCurrentZonedDateTime(ApplicationConstants.Web.Security.SERVER_TIMEZONE_ID);
    task.setCreationDate(creationDate);

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
}
