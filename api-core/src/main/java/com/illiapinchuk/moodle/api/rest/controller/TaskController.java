package com.illiapinchuk.moodle.api.rest.controller;

import com.illiapinchuk.moodle.common.mapper.SubmissionMapper;
import com.illiapinchuk.moodle.common.mapper.TaskMapper;
import com.illiapinchuk.moodle.common.validator.UserValidator;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.model.dto.SubmissionDto;
import com.illiapinchuk.moodle.model.dto.TaskDto;
import com.illiapinchuk.moodle.persistence.entity.Task;
import com.illiapinchuk.moodle.persistence.entity.TaskAttachment;
import com.illiapinchuk.moodle.persistence.entity.TaskSubmissionFile;
import com.illiapinchuk.moodle.service.CourseService;
import com.illiapinchuk.moodle.service.FileUploadService;
import com.illiapinchuk.moodle.service.TaskAttachmentService;
import com.illiapinchuk.moodle.service.TaskService;
import jakarta.annotation.Nonnull;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/** REST controller for task. */
@RestController
@RequestMapping(value = "/tasks")
@RequiredArgsConstructor
@Slf4j
public class TaskController {

  private final TaskMapper taskMapper;
  private final TaskService taskService;
  private final CourseService courseService;
  private final FileUploadService fileUploadService;
  private final SubmissionMapper submissionMapper;
  private final UserValidator userValidator;
  private final TaskAttachmentService taskAttachmentService;

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
  @PreAuthorize("hasAnyAuthority('ADMIN', 'DEVELOPER', 'TEACHER')")
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
   * Controller method for adding an attachment to a task.
   *
   * @param file The MultipartFile representing the attachment file.
   * @param taskId The ID of the task to which the attachment is being added.
   * @return ResponseEntity containing the updated TaskDto after adding the attachment.
   */
  @PreAuthorize("hasAnyAuthority('ADMIN', 'DEVELOPER', 'TEACHER')")
  @PostMapping("/{id}/upload")
  public ResponseEntity<TaskDto> addAttachmentToTask(
      @Nonnull @RequestParam("file") final MultipartFile file,
      @PathVariable("id") final String taskId) {
    final var task = taskService.getTaskById(taskId);

    // Upload the file and get the file name
    final var fileName = fileUploadService.uploadFile(file);

    // Create and save taskAttachment in MongoDB
    final var taskAttachment = TaskAttachment.builder().taskId(taskId).fileName(fileName).build();
    taskAttachmentService.saveTaskAttachment(taskAttachment);

    // Update task (add new file to attachments)
    task.getAttachments().add(taskAttachment);

    // Convert task to TaskDto
    final var taskDto = taskMapper.taskToTaskDto(task);

    // Update the task in the database
    final var taskAfterUpdate = taskService.updateTask(taskDto);

    // Get task attachments
    taskAfterUpdate.setAttachments(taskAttachmentService.getAttachmentsByTaskId(taskId));

    // Convert the updated task to TaskDto for response
    final var taskResponse = taskMapper.taskToTaskDto(taskAfterUpdate);

    return ResponseEntity.ok(taskResponse);
  }

  /**
   * Adds a submission to a task.
   *
   * @param submissionJson The {@link SubmissionDto} containing submission details.
   * @param files Optional list of {@link MultipartFile} objects representing submitted files.
   * @param taskId The ID of the task to which the submission is being added.
   * @return A {@link ResponseEntity} containing the updated {@link TaskDto} object.
   */
  @PostMapping("/{id}/submission")
  public ResponseEntity<TaskDto> addSubmissionToTask(
      @Valid @RequestParam(value = "submission") final String submissionJson,
      @RequestParam(value = "files", required = false) final List<MultipartFile> files,
      @PathVariable("id") final String taskId) {
    // Convert submissionJson to submissionDto
    final var submissionDto = submissionMapper.fromJson(submissionJson);
    // Convert submissionDto to submissionRequest
    final var submissionRequest = submissionMapper.submissionDtoToSubmission(submissionDto);

    // Retrieve the task by ID
    final var task = taskService.getTaskById(taskId);
    submissionRequest.setTask(task);

    // Get the user ID from submissionDto
    final var userId = submissionDto.getUserId();

    // Check if the user exists in the database
    if (!userValidator.isUserExistInDbById(userId)) {
      throw new UserNotFoundException(String.format("User with id: %s does not exist.", userId));
    }

    // Process submitted files, if any
    if (files != null && !files.isEmpty()) {
      var taskSubmissionFiles =
          files.stream()
              .map(
                  file -> {
                    // Upload the file and get the file name
                    var fileName = fileUploadService.uploadFile(file);
                    return TaskSubmissionFile.builder()
                        .taskId(taskId)
                        .userId(userId)
                        .fileName(fileName)
                        .build();
                  })
              .toList();
      submissionRequest.setSubmissionFiles(taskSubmissionFiles);
    }

    // Add the submission to the task's submissions list
    task.getSubmissions().add(submissionRequest);

    // Update the task in the database
    var updatedTask = taskService.updateTask(taskMapper.taskToTaskDto(task));

    // Convert the updated task to TaskDto
    var taskResponse = taskMapper.taskToTaskDto(updatedTask);

    return ResponseEntity.ok(taskResponse);
  }

  /**
   * Update an existing task.
   *
   * @param taskDto A {@link TaskDto} object representing the updated task information.
   * @return A {@link ResponseEntity} containing the updated {@link TaskDto} object.
   */
  @PreAuthorize("hasAnyAuthority('ADMIN', 'DEVELOPER', 'TEACHER')")
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
  @PreAuthorize("hasAnyAuthority('ADMIN', 'DEVELOPER', 'TEACHER')")
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteTaskById(@PathVariable("id") final String taskId) {
    taskService.deleteTaskById(taskId);
    log.info("Task with id: {} was deleted", taskId);
    return ResponseEntity.ok().build();
  }
}
