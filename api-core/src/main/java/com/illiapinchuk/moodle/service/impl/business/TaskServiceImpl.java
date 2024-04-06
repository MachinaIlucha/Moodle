package com.illiapinchuk.moodle.service.impl.business;

import com.illiapinchuk.moodle.common.mapper.TaskMapper;
import com.illiapinchuk.moodle.common.validator.TaskValidator;
import com.illiapinchuk.moodle.configuration.security.UserPermissionService;
import com.illiapinchuk.moodle.exception.UserDontHaveAccessToResource;
import com.illiapinchuk.moodle.model.dto.TaskDto;
import com.illiapinchuk.moodle.persistence.entity.Task;
import com.illiapinchuk.moodle.persistence.entity.TaskAttachment;
import com.illiapinchuk.moodle.persistence.repository.TaskRepository;
import com.illiapinchuk.moodle.service.business.FileUploadService;
import com.illiapinchuk.moodle.service.business.TaskAttachmentService;
import com.illiapinchuk.moodle.service.business.TaskService;
import com.illiapinchuk.moodle.service.crud.TaskCRUDService;
import jakarta.annotation.Nonnull;

import java.util.List;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/** Implementation of {@link TaskService} interface. */
@Service
@RequiredArgsConstructor
@Slf4j
public class TaskServiceImpl implements TaskService {

  private final TaskRepository taskRepository;

  private final TaskCRUDService taskCRUDService;
  private final TaskAttachmentService taskAttachmentService;
  private final FileUploadService fileUploadService;

  private final TaskMapper taskMapper;

  private final TaskValidator taskValidator;

  @Override
  @Transactional
  public TaskDto addAttachmentToTask(
      @Nonnull final MultipartFile file, @Nonnull final String taskId) {
    if (UserPermissionService.hasTeacherRole() && !taskValidator.isTeacherCanModifyTask(taskId)) {
      throw new UserDontHaveAccessToResource("Teacher can't modify the task.");
    }

    // Upload the file and get the file name
    final var fileName = fileUploadService.uploadFile(file);

    // Create and save taskAttachment in database
    final var taskAttachment = TaskAttachment.builder().taskId(taskId).fileName(fileName).build();
    taskAttachmentService.saveTaskAttachment(taskAttachment);

    // Update task (add new file to attachments)
    final var task = taskCRUDService.getTaskById(taskId);
    task.getAttachments().add(taskAttachment);

    final var taskDto = taskMapper.taskToTaskDto(task);
    final var updatedTask = taskCRUDService.updateTaskFromDto(taskDto);

    // Get task attachments
    updatedTask.setAttachments(taskAttachmentService.getAttachmentsByTaskId(taskId));

    return taskMapper.taskToTaskDto(updatedTask);
  }

  @Override
  public List<Task> getTasksByCourseId(@Nonnull final String courseId) {
    return taskRepository.getTasksByCourseId(courseId);
  }
}
