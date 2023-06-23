package com.illiapinchuk.moodle.common.mapper;

import com.illiapinchuk.moodle.model.dto.TaskDto;
import com.illiapinchuk.moodle.persistence.entity.Task;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

/** This interface defines methods for mapping between the {@link Task} and {@link TaskDto}. */
@Mapper(componentModel = "spring")
public interface TaskMapper {

  /**
   * Maps a {@link Task} object to a {@link TaskDto} object.
   *
   * @param task The {@link Task} object to be mapped.
   * @return The resulting {@link TaskDto} object.
   */
  @Mapping(source = "course.id", target = "courseId")
  TaskDto taskToTaskDto(Task task);

  /**
   * Maps a {@link TaskDto} object to a {@link Task} object.
   *
   * @param taskDto The {@link TaskDto} object to be mapped.
   * @return The resulting {@link Task} object.
   */
  Task taskDtoToTask(TaskDto taskDto);

  /**
   * This method updates the {@link Task} object with the data from the {@link TaskDto}.
   *
   * @param task The Task object to be updated.
   * @param taskDto The source of the updated data.
   */
  @Mapping(target = "creationDate", ignore = true)
  void updateTask(@MappingTarget Task task, TaskDto taskDto);
}
