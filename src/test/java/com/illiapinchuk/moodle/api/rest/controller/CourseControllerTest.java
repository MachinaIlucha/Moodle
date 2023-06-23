package com.illiapinchuk.moodle.api.rest.controller;

import static org.junit.jupiter.api.Assertions.*;
import com.illiapinchuk.moodle.common.mapper.CourseMapper;
import com.illiapinchuk.moodle.model.dto.CourseDto;
import com.illiapinchuk.moodle.persistence.entity.Course;
import com.illiapinchuk.moodle.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseControllerTest {

  private static final String COURSE_ID = "1";
  private static final String COURSE_NAME = "CourseName";
  private static final String COURSE_DESC = "CourseDesc";
  private static final String COURSE_NOT_FOUND = "Course not found";
  private static final String INVALID_COURSE_DATA = "Invalid course data";

  @Mock
  private CourseMapper courseMapper;

  @Mock
  private CourseService courseService;

  @InjectMocks
  private CourseController courseController;

  private CourseDto courseDto;
  private Course course;

  @BeforeEach
  void setUp() {
    courseDto = CourseDto.builder().id(COURSE_ID).name(COURSE_NAME).description(COURSE_DESC).build();
    course = Course.builder().id(COURSE_ID).name(COURSE_NAME).description(COURSE_DESC).build();
  }

  @Test
  void shouldGetCourseById() {
    when(courseService.getCourseById(COURSE_ID)).thenReturn(course);
    when(courseMapper.courseToCourseDto(course)).thenReturn(courseDto);

    ResponseEntity<CourseDto> responseEntity = courseController.getCourseById(COURSE_ID);

    assertEquals(200, responseEntity.getStatusCodeValue());
    assertEquals(courseDto, responseEntity.getBody());
    verify(courseService).getCourseById(COURSE_ID);
    verify(courseMapper).courseToCourseDto(course);
  }

  @Test
  void shouldCreateCourse() {
    when(courseMapper.courseDtoToCourse(courseDto)).thenReturn(course);
    when(courseService.createCourse(course)).thenReturn(course);
    when(courseMapper.courseToCourseDto(course)).thenReturn(courseDto);

    ResponseEntity<CourseDto> responseEntity = courseController.createCourse(courseDto);

    assertEquals(201, responseEntity.getStatusCodeValue());
    assertEquals(courseDto, responseEntity.getBody());
    verify(courseMapper).courseDtoToCourse(courseDto);
    verify(courseService).createCourse(course);
    verify(courseMapper).courseToCourseDto(course);
  }

  @Test
  void shouldUpdateCourse() {
    when(courseService.updateCourse(courseDto)).thenReturn(course);
    when(courseMapper.courseToCourseDto(course)).thenReturn(courseDto);

    ResponseEntity<CourseDto> responseEntity = courseController.updateCourse(courseDto);

    assertEquals(200, responseEntity.getStatusCodeValue());
    assertEquals(courseDto, responseEntity.getBody());
    verify(courseService).updateCourse(courseDto);
    verify(courseMapper).courseToCourseDto(course);
  }

  @Test
  void shouldDeleteCourseById() {
    doNothing().when(courseService).deleteCourseById(COURSE_ID);

    ResponseEntity<Void> responseEntity = courseController.deleteCourseById(COURSE_ID);

    assertEquals(200, responseEntity.getStatusCodeValue());
    verify(courseService).deleteCourseById(COURSE_ID);
  }

  // Handle corner case: Course Not Found
  @Test
  void shouldReturn404WhenCourseNotFound() {
    when(courseService.getCourseById(COURSE_ID)).thenThrow(new RuntimeException(COURSE_NOT_FOUND));

    assertThrows(RuntimeException.class, () -> courseController.getCourseById(COURSE_ID));
  }

  // Handle corner case: Course data invalid
  @Test
  void shouldReturn400WhenCourseDataInvalid() {
    when(courseService.createCourse(null)).thenThrow(new IllegalArgumentException(INVALID_COURSE_DATA));

    assertThrows(IllegalArgumentException.class, () -> courseController.createCourse(null));
  }
}
