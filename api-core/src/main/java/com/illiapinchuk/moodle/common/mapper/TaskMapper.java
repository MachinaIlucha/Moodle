package com.illiapinchuk.moodle.common.mapper;

import com.illiapinchuk.moodle.model.dto.SubmissionDto;
import com.illiapinchuk.moodle.model.dto.TaskDto;
import com.illiapinchuk.moodle.persistence.entity.Task;
import com.illiapinchuk.moodle.service.SubmissionService;
import java.util.Collections;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/** This interface defines methods for mapping between the {@link Task} and {@link TaskDto}. */
@Mapper(componentModel = "spring")
public abstract class TaskMapper {

  @Autowired
  private SubmissionService submissionService;

  @Autowired
  private SubmissionMapper submissionMapper;

  /**
   * Maps a {@link Task} object to a {@link TaskDto} object.
   *
   * @param task The {@link Task} object to be mapped.
   * @return The resulting {@link TaskDto} object.
   */
  @Mapping(source = "course.id", target = "courseId")
  @Mapping(target = "submissions", expression = "java(mapSubmissionIdsToDtos(task.getSubmissionIds()))")
  public abstract TaskDto taskToTaskDto(Task task);

  /**
   * Maps a {@link TaskDto} object to a {@link Task} object.
   *
   * @param taskDto The {@link TaskDto} object to be mapped.
   * @return The resulting {@link Task} object.
   */
  public abstract Task taskDtoToTask(TaskDto taskDto);

  /**
   * This method updates the {@link Task} object with the data from the {@link TaskDto}.
   *
   * @param task The Task object to be updated.
   * @param taskDto The source of the updated data.
   */
  @Mapping(target = "creationDate", ignore = true)
  @Mapping(target = "attachments", ignore = true)
  public abstract void updateTask(@MappingTarget Task task, TaskDto taskDto);

  protected List<SubmissionDto> mapSubmissionIdsToDtos(List<String> submissionIds) {
    if (submissionIds == null || submissionIds.isEmpty()) {
      return Collections.emptyList();
    }

    return submissionIds.stream()
        .map(
            id -> {
              final var submission = submissionService.getSubmissionById(id);
              return submissionMapper.submissionToSubmissionDto(submission);
            })
        .toList();
  }
}
