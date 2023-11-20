package com.illiapinchuk.moodle.api.rest.controller;

import com.illiapinchuk.moodle.common.mapper.CourseMapper;
import com.illiapinchuk.moodle.configuration.security.jwt.JwtUser;
import com.illiapinchuk.moodle.model.dto.CourseDto;
import com.illiapinchuk.moodle.persistence.entity.Course;
import com.illiapinchuk.moodle.service.CourseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** REST controller for Course. */
@RestController
@RequestMapping(value = "/courses")
@RequiredArgsConstructor
@Slf4j
public class CourseController {

  private final CourseMapper courseMapper;
  private final CourseService courseService;

  /**
   * Retrieves courses for a given student.
   *
   * @param studentId the id of the student whose courses are to be retrieved
   * @return a {@link ResponseEntity} containing a list of courses and a suitable HTTP status code
   */
  @PreAuthorize("hasAnyAuthority('ADMIN', 'DEVELOPER')")
  @GetMapping("/student/{studentId}/courses")
  public ResponseEntity<List<CourseDto>> getCoursesForStudent(
      @PathVariable("studentId") final Long studentId) {
    final var courses = courseService.getCoursesForUser(studentId);
    final var courseDtos = courses.stream().map(courseMapper::courseToCourseDto).toList();

    log.info("Retrieved courses for student with id: {}", studentId);
    return ResponseEntity.ok(courseDtos);
  }

  /**
   * Retrieves courses for the currently authenticated user.
   *
   * @return a {@link ResponseEntity} containing a list of courses and a suitable HTTP status code
   */
  @PreAuthorize("isAuthenticated()")
  @GetMapping("/my-courses")
  public ResponseEntity<List<CourseDto>> getMyCourses() {
    // Retrieve the user ID of the currently authenticated user
    final var authentication = SecurityContextHolder.getContext().getAuthentication();
    final var currentUserId = ((JwtUser) authentication.getPrincipal()).getId();

    // Fetch courses for the authenticated user
    final var courses = courseService.getCoursesForUser(currentUserId);
    final var courseDtos = courses.stream().map(courseMapper::courseToCourseDto).toList();

    log.info("Retrieved courses for authenticated user with id: {}", currentUserId);
    return ResponseEntity.ok(courseDtos);
  }

  /**
   * Adds students to a course.
   *
   * @param courseId the id of the course to which the students are to be added
   * @param studentIds a list of student ids to add to the course
   * @return a {@link ResponseEntity} with a suitable HTTP status code
   */
  @PreAuthorize("hasAnyAuthority('ADMIN', 'DEVELOPER', 'TEACHER')")
  @PostMapping("/{courseId}/students")
  public ResponseEntity<Void> addStudentsToCourse(
      @PathVariable("courseId") final String courseId,
      @RequestBody @NotEmpty final List<Long> studentIds) {
    courseService.addStudentsToCourse(courseId, studentIds);

    log.info("Students were added to course with id: {}", courseId);
    return ResponseEntity.ok().build();
  }

  @PreAuthorize("hasAnyAuthority('ADMIN', 'DEVELOPER', 'TEACHER')")
  @DeleteMapping("/{courseId}/students/{studentId}")
  public ResponseEntity<Void> removeStudentFromCourse(
      @PathVariable("courseId") final String courseId,
      @PathVariable("studentId") final Long studentId) {

    courseService.removeStudentFromCourse(courseId, studentId);

    log.info("Student with id: {} was removed from course with id: {}", studentId, courseId);
    return ResponseEntity.ok().build();
  }

  /**
   * Retrieves a course with the given id.
   *
   * @param courseId the id of the Course to retrieve
   * @return a {@link ResponseEntity} containing the {@link CourseDto} object and an HTTP status
   *     code
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
  @PreAuthorize("hasAnyAuthority('ADMIN', 'DEVELOPER', 'TEACHER')")
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
  @PreAuthorize("hasAnyAuthority('ADMIN', 'DEVELOPER', 'TEACHER')")
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
  @PreAuthorize("hasAnyAuthority('ADMIN', 'DEVELOPER', 'TEACHER')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteCourseById(@PathVariable("id") final String courseId) {
    courseService.deleteCourseById(courseId);
    log.info("Course with id: {} was deleted", courseId);
    return ResponseEntity.ok().build();
  }
}
