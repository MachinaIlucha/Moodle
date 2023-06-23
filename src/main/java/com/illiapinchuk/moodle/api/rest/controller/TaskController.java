package com.illiapinchuk.moodle.api.rest.controller;

import com.illiapinchuk.moodle.common.mapper.TaskMapper;
import com.illiapinchuk.moodle.model.dto.TaskDto;
import com.illiapinchuk.moodle.persistence.entity.Task;
import com.illiapinchuk.moodle.service.CourseService;
import com.illiapinchuk.moodle.service.TaskService;
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

/** REST controller for task. */
@RestController
@RequestMapping(value = "/tasks")
@RequiredArgsConstructor
@Slf4j
public class TaskController {

  private final TaskMapper taskMapper;
  private final TaskService taskService;
  private final CourseService courseService;

  /**
   * Retrieves a task with the given id.
   *
   * @param taskId the id of the Task to retrieve
   * @return a {@link ResponseEntity} containing the {@link TaskDto} object and an HTTP status code
   */
  @GetMapping("/{id}")
  public ResponseEntity<TaskDto> getTaskById(@PathVariable("id") final String taskId) {

    final var task = taskService.getTaskById(taskId);

    final var taskResponse = taskMapper.taskToTaskDto(task);

    log.info("Task with id: {} was found", taskId);
    return ResponseEntity.ok(taskResponse);
  }

  /**
   * Registers a new task by creating a new {@link Task} with the information provided in the
   * request body.
   *
   * @param taskDto a {@link TaskDto} object containing the task's information
   * @return a {@link ResponseEntity} object with a status of 201 (Created) and the created {@link
   *     TaskDto} object in the body.
   */
  @PostMapping
  public ResponseEntity<TaskDto> createTask(@Valid @RequestBody final TaskDto taskDto) {
    final var taskRequest = taskMapper.taskDtoToTask(taskDto);
    taskRequest.setCourse(courseService.getCourseById(taskDto.getCourseId()));
    final var task = taskService.createTask(taskRequest);
    final var taskResponse = taskMapper.taskToTaskDto(task);

    log.info("New task was created with id: {}", task.getId());
    return ResponseEntity.status(HttpStatus.CREATED).body(taskResponse);
  }

  /**
   * Update an existing task.
   *
   * @param taskDto A {@link TaskDto} object representing the updated task information.
   * @return A {@link ResponseEntity} containing the updated {@link TaskDto} object.
   */
  @PutMapping
  public ResponseEntity<TaskDto> updateTask(@Valid @RequestBody final TaskDto taskDto) {
    final var task = taskService.updateTask(taskDto);
    final var taskResponse = taskMapper.taskToTaskDto(task);

    log.info("Task with id: {} was updated", task.getId());
    return ResponseEntity.ok().body(taskResponse);
  }

  /**
   * Deletes the task with the given id.
   *
   * @param taskId the id of the task to delete
   * @return a response with an HTTP status of OK if the task was successfully deleted, or an HTTP
   *     status of NOT_FOUND if the task was not found
   */
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTaskById(@PathVariable("id") final String taskId) {
    taskService.deleteTaskById(taskId);
    log.info("Task with id: {} was deleted", taskId);
    return ResponseEntity.ok().build();
  }
}
