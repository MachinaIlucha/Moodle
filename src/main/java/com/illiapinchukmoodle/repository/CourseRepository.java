package com.illiapinchukmoodle.repository;

import com.illiapinchukmoodle.data.model.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author Illia Pinchuk
 */
public interface CourseRepository extends JpaRepository<Course, Long> {
    @Query("select course from Course course join fetch course.courseUsers")
    List<Course> getCoursesWithUsers();

    @Query("select course from Course course join fetch course.courseUsers where course.id = :courseId")
    Optional<Course> getCourseWithUsers(Long courseId);

    @Query("select course from Course course join fetch course.tasks where course.id = :courseId")
    Course getCourseWithTasks(Long courseId);
}
