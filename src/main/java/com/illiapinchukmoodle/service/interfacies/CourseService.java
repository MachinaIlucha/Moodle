package com.illiapinchukmoodle.service.interfacies;

import com.illiapinchukmoodle.data.model.Course;

import java.util.List;
import java.util.Optional;

/**
 * Service for {@link com.illiapinchukmoodle.data.model.Course}
 * @author Illia Pinchuk
 */
public interface CourseService {
    List<Course> getAllCourses();
    Course createCourse(Course course);
    Optional<Course> getCourseById(Long courseId);
    Course updateCourse(Course courseRequest, Long courseId);
    void deleteCourse(Long courseId);
    void deleteCourseFromUser(Long courseId, Long userId);
    Course addCourseToUser(Long courseId, Long userId);
    Integer getCourseProgressByUser(Long courseId, Long userId);
    Optional<Course> getCourseWithUsers(Long courseId);
    Optional<Course> getCourseWithTasks(Long courseId);
}
