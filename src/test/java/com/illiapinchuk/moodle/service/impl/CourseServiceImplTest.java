package com.illiapinchuk.moodle.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.illiapinchuk.moodle.common.mapper.CourseMapper;
import com.illiapinchuk.moodle.common.validator.CourseValidator;
import com.illiapinchuk.moodle.exception.CourseNotFoundException;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.model.dto.CourseDto;
import com.illiapinchuk.moodle.persistence.entity.Course;
import com.illiapinchuk.moodle.persistence.repository.CourseRepository;
import com.illiapinchuk.moodle.service.TaskService;
import java.util.Collections;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {
  @Mock private CourseRepository courseRepository;

  @Mock private CourseMapper courseMapper;

  @Mock private CourseValidator courseValidator;

  @Mock private TaskService taskService;

  @InjectMocks private CourseServiceImpl courseService;

  @Test
  void getCourseById_courseNotFound() {
    when(courseRepository.findById(anyString())).thenReturn(Optional.empty());
    assertThrows(CourseNotFoundException.class, () -> courseService.getCourseById("id"));
  }

  @Test
  void getCourseById_courseFound() {
    Course course = new Course();
    when(courseRepository.findById(anyString())).thenReturn(Optional.of(course));
    when(taskService.getTasksByCourseId(anyString())).thenReturn(Collections.emptyList());
    assertEquals(course, courseService.getCourseById("id"));
  }

  @Test
  void createCourse_authorNotExists() {
    Course course = new Course();
    when(courseValidator.isAuthorsExistsInDbByIds(any())).thenReturn(false);
    assertThrows(UserNotFoundException.class, () -> courseService.createCourse(course));
  }

  @Test
  void createCourse_authorExists() {
    Course course = new Course();
    when(courseValidator.isAuthorsExistsInDbByIds(any())).thenReturn(true);
    when(courseRepository.save(any())).thenReturn(course);
    assertEquals(course, courseService.createCourse(course));
  }

  @Test
  void updateCourse_courseNotExists() {
    CourseDto courseDto = new CourseDto();
    courseDto.setId("id");
    when(courseValidator.isCourseExistsInDbById(anyString())).thenReturn(false);
    assertThrows(CourseNotFoundException.class, () -> courseService.updateCourse(courseDto));
  }

  @Test
  void updateCourse_courseExists() {
    Course course = new Course();
    CourseDto courseDto = new CourseDto();
    courseDto.setId("id");
    when(courseValidator.isCourseExistsInDbById(anyString())).thenReturn(true);
    when(courseRepository.findById(anyString())).thenReturn(Optional.of(course));
    when(courseRepository.save(any())).thenReturn(course);
    assertEquals(course, courseService.updateCourse(courseDto));
  }

  @Test
  void deleteCourseById() {
    doNothing().when(courseRepository).deleteById(anyString());
    assertDoesNotThrow(() -> courseService.deleteCourseById("id"));
  }
}
