package com.illiapinchuk.moodle.api.rest.controller;

import com.illiapinchuk.moodle.common.mapper.CourseMapper;
import com.illiapinchuk.moodle.model.dto.CourseDto;
import com.illiapinchuk.moodle.persistence.entity.Course;
import com.illiapinchuk.moodle.service.CourseService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** REST controller for Course. */
@RestController
@RequestMapping(value = "/courses")
@RequiredArgsConstructor
@Slf4j
public class CourseController {

  private final CourseMapper courseMapper;
  private final CourseService courseService;

  /**
   * Retrieves a course with the given id.
   *
   * @param courseId the id of the Course to retrieve
   * @return a {@link ResponseEntity} containing the {@link CourseDto} object and an HTTP status code
   */
  @GetMapping("/{id}")
  public ResponseEntity<CourseDto> getCourseById(@PathVariable("id") final String courseId) {

    final var course = courseService.getCourseById(courseId);

    final var courseResponse = courseMapper.courseToCourseDto(course);

    log.info("Course with id: {} was found", courseId);
    return ResponseEntity.ok(courseResponse);
  }

  /**
   * Create a new course by creating a new {@link Course} with the information provided in the
   * request body.
   *
   * @param courseDto a {@link CourseDto} object containing the course information
   * @return a {@link ResponseEntity} object with a status of 201 (Created) and the created {@link
   *     CourseDto} object in the body.
   */
  @PostMapping
  public ResponseEntity<CourseDto> createCourse(@Valid @RequestBody final CourseDto courseDto) {
    final var courseRequest = courseMapper.courseDtoToCourse(courseDto);
    final var course = courseService.createCourse(courseRequest);
    final var courseResponse = courseMapper.courseToCourseDto(course);

    log.info("New course was created with id: {}", course.getId());
    return ResponseEntity.status(HttpStatus.CREATED).body(courseResponse);
  }

  /**
   * Update an existing course.
   *
   * @param courseDto A {@link CourseDto} object representing the updated course information.
   * @return A {@link ResponseEntity} containing the updated {@link CourseDto} object.
   */
  @PutMapping
  public ResponseEntity<CourseDto> updateCourse(@Valid @RequestBody final CourseDto courseDto) {
    final var course = courseService.updateCourse(courseDto);
    final var courseResponse = courseMapper.courseToCourseDto(course);

    log.info("Course with id: {} was updated", course.getId());
    return ResponseEntity.ok().body(courseResponse);
  }

  /**
   * Deletes the course with the given id.
   *
   * @param courseId the id of the course to delete
   * @return a response with an HTTP status of OK if the course was successfully deleted, or an HTTP
   *     status of NOT_FOUND if the course was not found
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCourseById(@PathVariable("id") final String courseId) {
    courseService.deleteCourseById(courseId);
    log.info("Course with id: {} was deleted", courseId);
    return ResponseEntity.ok().build();
  }
}
