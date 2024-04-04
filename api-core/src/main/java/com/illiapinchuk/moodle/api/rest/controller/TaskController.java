package com.illiapinchuk.moodle.api.rest.controller;

import com.illiapinchuk.moodle.common.mapper.SubmissionMapper;
import com.illiapinchuk.moodle.common.mapper.TaskMapper;
import com.illiapinchuk.moodle.model.dto.GradeDto;
import com.illiapinchuk.moodle.model.dto.SubmissionDto;
import com.illiapinchuk.moodle.model.dto.TaskDto;
import com.illiapinchuk.moodle.persistence.entity.Task;
import com.illiapinchuk.moodle.service.business.CourseTaskFacade;
import com.illiapinchuk.moodle.service.business.GradeService;
import com.illiapinchuk.moodle.service.business.SubmissionService;
import com.illiapinchuk.moodle.service.business.TaskService;
import com.illiapinchuk.moodle.service.business.TaskSubmissionFacade;
import com.illiapinchuk.moodle.service.crud.TaskCRUDService;
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

  private final TaskCRUDService taskCRUDService;

  private final TaskService taskService;
  private final GradeService gradeService;
  private final SubmissionService submissionService;

  private final TaskSubmissionFacade taskSubmissionFacade;
  private final CourseTaskFacade courseTaskFacade;

  private final TaskMapper taskMapper;
  private final SubmissionMapper submissionMapper;

  /**
   * Retrieves a task with the given id.
   *
   * @param taskId the id of the Task to retrieve
   * @return a {@link ResponseEntity} containing the {@link TaskDto} object and an HTTP status code
   */
  @GetMapping("/{id}")
  public ResponseEntity<TaskDto> getTaskById(@PathVariable("id") final String taskId) {
    final var task = taskCRUDService.getTaskById(taskId);
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
    final var task = courseTaskFacade.createTaskWithCourse(taskDto);

    log.info("New task was created with id: {}", task.getId());
    return ResponseEntity.status(HttpStatus.CREATED).body(task);
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
    final var taskWithAttachments = taskService.addAttachmentToTask(file, taskId);

    log.info("Attachment was added to task with id: {}", taskId);
    return ResponseEntity.ok(taskWithAttachments);
  }

  /**
   * Adds a submission to a task.
   *
   * @param submissionJson The {@link SubmissionDto} containing submission details.
   * @param files Optional list of {@link MultipartFile} objects representing submitted files.
   * @param taskId The ID of the task to which the submission is being added.
   * @return A {@link ResponseEntity} containing the updated {@link TaskDto} object.
   */
  @PostMapping("/{taskId}/submission")
  public ResponseEntity<TaskDto> addSubmissionToTask(
      @Valid @RequestParam(value = "submission") final String submissionJson,
      @RequestParam(value = "files", required = false) final List<MultipartFile> files,
      @PathVariable("taskId") final String taskId) {
    final var taskWithSubmission =
        taskSubmissionFacade.addSubmissionToTask(submissionJson, files, taskId);

    log.info("Submission was added to task with id: {}", taskId);
    return ResponseEntity.ok(taskWithSubmission);
  }

  /**
   * Grades a submission for a task.
   *
   * @param taskId The ID of the task for which the submission is being graded.
   * @param submissionId The ID of the submission being graded.
   * @param gradeDto The {@link GradeDto} object containing the grade information.
   * @return A {@link ResponseEntity} with an HTTP status of OK.
   */
  @PreAuthorize("hasAnyAuthority('ADMIN', 'DEVELOPER', 'TEACHER')")
  @PostMapping("/{taskId}/submissions/{submissionId}/grade")
  public ResponseEntity<Void> gradeSubmission(
      @PathVariable("taskId") final String taskId,
      @PathVariable("submissionId") final String submissionId,
      @RequestBody @Valid final GradeDto gradeDto) {

    gradeService.gradeSubmission(taskId, submissionId, gradeDto);
    log.info("Submission with id: {} for task with id: {} was graded", submissionId, taskId);
    return ResponseEntity.ok().build();
  }

  /**
   * Retrieves all submissions for a task.
   *
   * @param taskId The ID of the task for which submissions are being retrieved.
   * @return A {@link ResponseEntity} containing a list of {@link SubmissionDto} objects.
   */
  @PreAuthorize("hasAnyAuthority('ADMIN', 'DEVELOPER', 'TEACHER')")
  @PostMapping("/{taskId}/submissions")
  public ResponseEntity<List<SubmissionDto>> getAllSubmissionsForTask(
      @PathVariable("taskId") final String taskId) {
    final var submissionsForTask = submissionService.getAllSubmissionsForTask(taskId);

    final var submissionsForTaskResponse =
        submissionsForTask.stream().map(submissionMapper::submissionToSubmissionDto).toList();

    log.info("Retrieved all submissions for task with id: {}", taskId);
    return ResponseEntity.ok(submissionsForTaskResponse);
  }

  /**
   * Retrieves all submissions for a task and student.
   *
   * @param taskId The ID of the task for which submissions are being retrieved.
   * @param studentId The ID of the student for which submissions are being retrieved.
   * @return A {@link ResponseEntity} containing a list of {@link SubmissionDto} objects.
   */
  @PostMapping("/{taskId}/students/{studentId}/submissions")
  public ResponseEntity<List<SubmissionDto>> getAllSubmissionsForTaskAndStudent(
      @PathVariable("taskId") final String taskId,
      @PathVariable("studentId") final Long studentId) {
    final var submissionsForTask =
        submissionService.getSubmissionsByTaskIdAndStudentId(taskId, studentId);

    final var submissionsForTaskResponse =
        submissionsForTask.stream().map(submissionMapper::submissionToSubmissionDto).toList();

    log.info(
        "Retrieved all submissions for task with id: {} and student with id: {}",
        taskId,
        studentId);
    return ResponseEntity.ok(submissionsForTaskResponse);
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
    final var task = taskCRUDService.updateTaskFromDto(taskDto);
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
    taskCRUDService.deleteTaskById(taskId);
    log.info("Task with id: {} was deleted", taskId);
    return ResponseEntity.ok().build();
  }
}
