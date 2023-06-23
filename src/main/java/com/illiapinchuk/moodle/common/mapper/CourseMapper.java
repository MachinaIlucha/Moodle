package com.illiapinchuk.moodle.common.mapper;

import com.illiapinchuk.moodle.model.dto.CourseDto;
import com.illiapinchuk.moodle.persistence.entity.Course;
import com.illiapinchuk.moodle.persistence.entity.Task;
import java.util.Collections;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/** This interface defines methods for mapping between the {@link Course} and {@link CourseDto}. */
@Mapper(componentModel = "spring")
public interface CourseMapper {

  /**
   * Maps a {@link Course} object to a {@link CourseDto} object.
   *
   * @param course The {@link Course} object to be mapped.
   * @return The resulting {@link CourseDto} object.
   */
  @Mapping(target = "tasks", expression = "java(taskListToStringList(course.getTasks()))")
  CourseDto courseToCourseDto(Course course);

  /**
   * Maps a {@link CourseDto} object to a {@link Course} object.
   *
   * @param courseDto The {@link CourseDto} object to be mapped.
   * @return The resulting {@link Course} object.
   */
  @Mapping(target = "tasks", ignore = true)
  Course courseDtoToCourse(CourseDto courseDto);

  /**
   * This method updates the {@link Course} object with the data from the {@link CourseDto}.
   *
   * @param course The Course object to be updated.
   * @param courseDto The source of the updated data.
   */
  @Mapping(target = "tasks", ignore = true)
  void updateCourse(@MappingTarget Course course, CourseDto courseDto);

  default List<String> taskListToStringList(List<Task> tasks) {
    return tasks == null ? Collections.emptyList() : tasks.stream().map(Task::getId).toList();
  }
}
