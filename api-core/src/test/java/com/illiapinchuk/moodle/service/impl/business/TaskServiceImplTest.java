package com.illiapinchuk.moodle.service.impl.business;

import static com.illiapinchuk.moodle.common.TestConstants.FileConstants.VALID_FILENAME;
import static com.illiapinchuk.moodle.common.TestConstants.TaskConstants.TASK_ID;
import static com.illiapinchuk.moodle.common.TestConstants.TaskConstants.VALID_TASK_1;
import static com.illiapinchuk.moodle.common.TestConstants.TaskConstants.VALID_TASK_DTO;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.illiapinchuk.moodle.common.TestConstants;
import com.illiapinchuk.moodle.common.mapper.TaskMapper;
import com.illiapinchuk.moodle.exception.CannotWriteToS3Exception;
import com.illiapinchuk.moodle.exception.TaskNotFoundException;
import com.illiapinchuk.moodle.model.dto.TaskDto;
import com.illiapinchuk.moodle.persistence.entity.Task;
import com.illiapinchuk.moodle.persistence.entity.TaskAttachment;
import com.illiapinchuk.moodle.persistence.repository.TaskRepository;

import com.illiapinchuk.moodle.service.business.FileUploadService;
import com.illiapinchuk.moodle.service.business.TaskAttachmentService;
import com.illiapinchuk.moodle.service.crud.TaskCRUDService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

  @Mock private TaskRepository taskRepository;

  @Mock private TaskCRUDService taskCRUDService;

  @Mock private TaskAttachmentService taskAttachmentService;

  @Mock private FileUploadService fileUploadService;

  @Mock private TaskMapper taskMapper;

  @Mock private MultipartFile file;

  @InjectMocks private TaskServiceImpl taskService;

  @Test
  void addAttachmentToTask_Success() {
    when(fileUploadService.uploadFile(file)).thenReturn(VALID_FILENAME);
    when(taskCRUDService.getTaskById(TASK_ID)).thenReturn(VALID_TASK_1);
    when(taskMapper.taskToTaskDto(any(Task.class))).thenReturn(VALID_TASK_DTO);
    when(taskCRUDService.updateTaskFromDto(any(TaskDto.class))).thenReturn(VALID_TASK_1);

    final var result = taskService.addAttachmentToTask(file, TASK_ID);

    assertThat(result).isNotNull();

    verify(fileUploadService).uploadFile(file);
    verify(taskCRUDService).getTaskById(TASK_ID);
    verify(taskCRUDService).updateTaskFromDto(any(TaskDto.class));
    verify(taskAttachmentService).saveTaskAttachment(any(TaskAttachment.class));
    verify(taskAttachmentService).getAttachmentsByTaskId(TASK_ID);
  }

  @Test
  void addAttachmentToTask_FileUploadFails_ThrowsException() {
    when(fileUploadService.uploadFile(file)).thenThrow(new CannotWriteToS3Exception("Upload failed"));

    assertThrows(CannotWriteToS3Exception.class, () -> {
      taskService.addAttachmentToTask(file, TASK_ID);
    });

    verify(fileUploadService).uploadFile(file);
    verify(taskCRUDService, never()).getTaskById(TASK_ID);
  }

  @Test
  void addAttachmentToTask_TaskNotFound_ThrowsException() {
    when(fileUploadService.uploadFile(file)).thenReturn("fileName");
    when(taskCRUDService.getTaskById(TASK_ID)).thenThrow(new TaskNotFoundException("Task not found"));

    assertThrows(TaskNotFoundException.class, () -> {
      taskService.addAttachmentToTask(file, TASK_ID);
    });

    verify(fileUploadService).uploadFile(file);
    verify(taskCRUDService).getTaskById(TASK_ID);
  }

  @Test
  void testGetTasksByCourseId_ValidCourseId_TasksFound() {
    when(taskRepository.getTasksByCourseId(TestConstants.CourseConstants.VALID_COURSE_ID))
        .thenReturn(TestConstants.TaskConstants.LIST_OF_VALID_TASKS);

    final var result =
        taskService.getTasksByCourseId(TestConstants.CourseConstants.VALID_COURSE_ID);

    assertEquals(TestConstants.TaskConstants.LIST_OF_VALID_TASKS, result);
    verify(taskRepository).getTasksByCourseId(TestConstants.CourseConstants.VALID_COURSE_ID);
  }
}
