package com.illiapinchuk.moodle.service.impl.business;

import com.illiapinchuk.moodle.common.mapper.CourseMapper;
import com.illiapinchuk.moodle.common.mapper.TaskMapper;
import com.illiapinchuk.moodle.common.validator.CourseValidator;
import com.illiapinchuk.moodle.common.validator.UserValidator;
import com.illiapinchuk.moodle.configuration.security.UserPermissionService;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.model.dto.TaskDto;
import com.illiapinchuk.moodle.persistence.entity.Course;
import com.illiapinchuk.moodle.service.crud.CourseCRUDService;
import com.illiapinchuk.moodle.service.crud.TaskCRUDService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;

import static com.illiapinchuk.moodle.common.TestConstants.CourseTaskFacadeImplTestConstants.INVALID_COURSE_ID;
import static com.illiapinchuk.moodle.common.TestConstants.CourseTaskFacadeImplTestConstants.INVALID_STUDENT_ID;
import static com.illiapinchuk.moodle.common.TestConstants.CourseTaskFacadeImplTestConstants.VALID_COURSE;
import static com.illiapinchuk.moodle.common.TestConstants.CourseTaskFacadeImplTestConstants.VALID_COURSE_DTO;
import static com.illiapinchuk.moodle.common.TestConstants.CourseTaskFacadeImplTestConstants.VALID_COURSE_ID;
import static com.illiapinchuk.moodle.common.TestConstants.CourseTaskFacadeImplTestConstants.VALID_STUDENT_ID;
import static com.illiapinchuk.moodle.common.TestConstants.CourseTaskFacadeImplTestConstants.VALID_TASK;
import static com.illiapinchuk.moodle.common.TestConstants.CourseTaskFacadeImplTestConstants.VALID_TASK_DTO;
import static com.illiapinchuk.moodle.common.TestConstants.CourseTaskFacadeImplTestConstants.VALID_TASK_ID;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseTaskFacadeImplTest {

  @Mock private CourseCRUDService courseCRUDService;
  @Mock private CourseMapper courseMapper;
  @Mock private TaskCRUDService taskCRUDService;
  @Mock private UserValidator userValidator;
  @Mock private TaskMapper taskMapper;
  @Mock private CourseValidator courseValidator;
  @InjectMocks private CourseTaskFacadeImpl courseTaskFacade;

  @Test
  void getCourseWithTasks_CourseDoesNotExist_ReturnsNull() {
    final var courseId = INVALID_COURSE_ID;

    when(courseCRUDService.getCourseById(courseId)).thenReturn(null);

    final var result = courseTaskFacade.getCourseWithTasks(courseId);

    assertNull(result);
  }

  @Test
  void getCourseWithTasks_CourseExists_ReturnsCourseDto() {
    final var courseId = VALID_COURSE_ID;
    final var course = VALID_COURSE;
    final var expectedCourseDto = VALID_COURSE_DTO;

    when(courseCRUDService.getCourseById(courseId)).thenReturn(course);
    when(courseMapper.courseToCourseDto(course)).thenReturn(expectedCourseDto);

    final var result = courseTaskFacade.getCourseWithTasks(courseId);

    assertEquals(expectedCourseDto, result);
  }

  @Test
  void getCourseWithTasks_CourseDoesNotExist_ThrowsException() {
    final var courseId = INVALID_COURSE_ID;

    when(courseCRUDService.getCourseById(courseId))
        .thenThrow(new RuntimeException("Course not found"));

    assertThrows(RuntimeException.class, () -> courseTaskFacade.getCourseWithTasks(courseId));
  }

  @Test
  void deleteCourseByIdWithTasks_CourseExists_DeletesCourse() {
    final var courseId = VALID_COURSE_ID;
    final var task = VALID_TASK;
    final var course = Course.builder().id(courseId).tasks(new ArrayList<>()).build();
    course.getTasks().add(task);

    when(courseCRUDService.getCourseById(courseId)).thenReturn(course);

    courseTaskFacade.deleteCourseByIdWithTasks(courseId);

    verify(taskCRUDService).deleteTaskById(task.getId());
    verify(courseCRUDService).deleteCourseById(courseId);
  }

  @Test
  void deleteCourseByIdWithTasks_CourseDoesNotExist_ThrowsException() {
    final var courseId = INVALID_COURSE_ID;

    when(courseCRUDService.getCourseById(courseId))
        .thenThrow(new RuntimeException("Course not found"));

    assertThrows(
        RuntimeException.class, () -> courseTaskFacade.deleteCourseByIdWithTasks(courseId));
  }

  @Test
  void createTaskWithCourse_AuthorDoesNotExist_ThrowsException() {
    try (MockedStatic<UserPermissionService> mocked = mockStatic(UserPermissionService.class)) {
      mocked.when(UserPermissionService::hasTeacherRole).thenReturn(true);
      when(courseValidator.isTeacherCanModifyCourse(anyString())).thenReturn(true);
      final var taskDto = VALID_TASK_DTO;
      final var task = VALID_TASK;
      final var course = VALID_COURSE;

      when(taskMapper.taskDtoToTask(taskDto)).thenReturn(task);
      when(courseCRUDService.getCourseById(taskDto.getCourseId())).thenReturn(course);
      when(userValidator.isUserExistInDbById(task.getAuthorId())).thenReturn(false);

      assertThrows(
          UserNotFoundException.class, () -> courseTaskFacade.createTaskWithCourse(taskDto));
    }
  }

  @Test
  void createTaskWithCourse_ValidCourseAndAuthor_CreatesTask() {
    try (MockedStatic<UserPermissionService> mocked = mockStatic(UserPermissionService.class)) {
      mocked.when(UserPermissionService::hasTeacherRole).thenReturn(true);
      when(courseValidator.isTeacherCanModifyCourse(anyString())).thenReturn(true);

      final var taskDto = VALID_TASK_DTO;
      final var task = VALID_TASK;
      final var course = Course.builder().id(VALID_COURSE_ID).tasks(new ArrayList<>()).build();

      when(courseCRUDService.getCourseById(taskDto.getCourseId())).thenReturn(course);
      when(userValidator.isUserExistInDbById(task.getAuthorId())).thenReturn(true);
      when(taskMapper.taskDtoToTask(taskDto)).thenReturn(task);
      when(taskCRUDService.createTask(task)).thenReturn(task);
      when(taskMapper.taskToTaskDto(task)).thenReturn(taskDto);

      final var result = courseTaskFacade.createTaskWithCourse(taskDto);

      assertEquals(taskDto, result);
      verify(courseCRUDService, times(2)).getCourseById(taskDto.getCourseId());
      verify(userValidator).isUserExistInDbById(task.getAuthorId());
      verify(taskMapper).taskDtoToTask(taskDto);
      verify(taskCRUDService).createTask(task);
      verify(taskMapper).taskToTaskDto(task);
    }
  }

  @Test
  void createTaskWithCourse_InvalidCourse_ThrowsException() {
    final var taskDto = TaskDto.builder().courseId(INVALID_COURSE_ID).authorId(1L).build();

    when(courseCRUDService.getCourseById(taskDto.getCourseId()))
        .thenThrow(new RuntimeException("Course not found"));

    assertThrows(RuntimeException.class, () -> courseTaskFacade.createTaskWithCourse(taskDto));
  }

  @Test
  void createTaskWithCourse_InvalidAuthor_ThrowsException() {
    final var taskDto =
        TaskDto.builder().courseId(VALID_COURSE_ID).authorId(INVALID_STUDENT_ID).build();

    assertThrows(RuntimeException.class, () -> courseTaskFacade.createTaskWithCourse(taskDto));
  }

  @Test
  void addTaskToCourse_ValidCourseAndTask_AddsTaskToCourse() {
    final var courseId = VALID_COURSE_ID;
    final var taskId = VALID_TASK_ID;
    final var course = Course.builder().id(courseId).tasks(new ArrayList<>()).build();
    final var task = VALID_TASK;

    when(courseCRUDService.getCourseById(courseId)).thenReturn(course);
    when(taskCRUDService.getTaskById(taskId)).thenReturn(task);

    courseTaskFacade.addTaskToCourse(courseId, taskId);

    verify(courseCRUDService).getCourseById(courseId);
    verify(taskCRUDService).getTaskById(taskId);
    verify(courseCRUDService).updateCourse(course);
  }

  @Test
  void addTaskToCourse_InvalidCourse_ThrowsException() {
    final var courseId = INVALID_COURSE_ID;
    final var taskId = VALID_TASK_ID;

    when(courseCRUDService.getCourseById(courseId))
        .thenThrow(new RuntimeException("Course not found"));

    assertThrows(RuntimeException.class, () -> courseTaskFacade.addTaskToCourse(courseId, taskId));
  }

  @Test
  void addTaskToCourse_InvalidTask_ThrowsException() {
    final var courseId = VALID_COURSE_ID;
    final var taskId = "invalidTaskId";

    when(taskCRUDService.getTaskById(taskId)).thenThrow(new RuntimeException("Task not found"));

    assertThrows(RuntimeException.class, () -> courseTaskFacade.addTaskToCourse(courseId, taskId));
  }

  @Test
  void addTaskToCourse_TaskDoesNotExist_ThrowsException() {
    final var courseId = VALID_COURSE_ID;
    final var taskId = "invalidTaskId";

    when(courseCRUDService.getCourseById(courseId)).thenReturn(VALID_COURSE);
    when(taskCRUDService.getTaskById(taskId)).thenReturn(null);

    assertThrows(RuntimeException.class, () -> courseTaskFacade.addTaskToCourse(courseId, taskId));
  }

  @Test
  void addTaskToCourse_CourseDoesNotExist_ThrowsException() {
    final var courseId = INVALID_COURSE_ID;
    final var taskId = VALID_TASK_ID;

    when(courseCRUDService.getCourseById(courseId)).thenReturn(null);

    assertThrows(RuntimeException.class, () -> courseTaskFacade.addTaskToCourse(courseId, taskId));
  }

  @Test
  void getOverallGradeForStudentInCourse_InvalidCourse_ThrowsException() {
    final var courseId = INVALID_COURSE_ID;
    final var studentId = VALID_STUDENT_ID;

    assertThrows(
        RuntimeException.class,
        () -> courseTaskFacade.getOverallGradeForStudentInCourse(courseId, studentId));
  }

  @Test
  void getOverallGradeForStudentInCourse_InvalidStudent_ThrowsException() {
    final var courseId = VALID_COURSE_ID;
    final var studentId = INVALID_STUDENT_ID;

    when(userValidator.isUserExistInDbById(studentId)).thenReturn(false);

    assertThrows(
        RuntimeException.class,
        () -> courseTaskFacade.getOverallGradeForStudentInCourse(courseId, studentId));
  }

  @Test
  void getOverallGradeForStudentInCourse_StudentDoesNotExist_ThrowsException() {
    final var courseId = VALID_COURSE_ID;
    final var studentId = INVALID_STUDENT_ID;

    when(userValidator.isUserExistInDbById(studentId)).thenReturn(false);

    assertThrows(
        UserNotFoundException.class,
        () -> courseTaskFacade.getOverallGradeForStudentInCourse(courseId, studentId));
  }

  @Test
  void getOverallGradeForStudentInCourse_ValidCourseAndStudent_ReturnsGrade() {
    try (MockedStatic<UserPermissionService> mocked = mockStatic(UserPermissionService.class)) {
      mocked.when(UserPermissionService::hasTeacherRole).thenReturn(true);
      when(courseValidator.isTeacherCanModifyCourse(anyString())).thenReturn(true);
      final var courseId = VALID_COURSE_ID;
      final var studentId = VALID_STUDENT_ID;
      final var course = Course.builder().id(VALID_COURSE_ID).tasks(new ArrayList<>()).build();

      when(courseCRUDService.getCourseById(courseId)).thenReturn(course);
      when(userValidator.isUserExistInDbById(studentId)).thenReturn(true);

      final var result = courseTaskFacade.getOverallGradeForStudentInCourse(courseId, studentId);

      assertNotNull(result);
    }
  }

  @Test
  void getOverallGradeForStudentInCourse_CourseDoesNotExist_ThrowsException() {
    final var courseId = INVALID_COURSE_ID;
    final var studentId = VALID_STUDENT_ID;

    assertThrows(
        RuntimeException.class,
        () -> courseTaskFacade.getOverallGradeForStudentInCourse(courseId, studentId));
  }
}
