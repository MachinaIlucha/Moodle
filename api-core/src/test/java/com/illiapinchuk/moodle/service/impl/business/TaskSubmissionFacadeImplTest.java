package com.illiapinchuk.moodle.service.impl.business;

import com.illiapinchuk.moodle.common.mapper.SubmissionMapper;
import com.illiapinchuk.moodle.common.mapper.TaskMapper;
import com.illiapinchuk.moodle.common.validator.TaskValidator;
import com.illiapinchuk.moodle.common.validator.UserValidator;
import com.illiapinchuk.moodle.exception.TaskNotFoundException;
import com.illiapinchuk.moodle.exception.UserNotFoundException;
import com.illiapinchuk.moodle.model.dto.SubmissionDto;
import com.illiapinchuk.moodle.model.dto.TaskDto;
import com.illiapinchuk.moodle.persistence.entity.Submission;
import com.illiapinchuk.moodle.persistence.entity.Task;
import com.illiapinchuk.moodle.service.business.CourseService;
import com.illiapinchuk.moodle.service.business.FileUploadService;
import com.illiapinchuk.moodle.service.crud.GradeCRUDService;
import com.illiapinchuk.moodle.service.crud.SubmissionCRUDService;
import com.illiapinchuk.moodle.service.crud.TaskCRUDService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskSubmissionFacadeImplTest {

  @Mock private TaskCRUDService taskCRUDService;
  @Mock private CourseService courseService;
  @Mock private FileUploadService fileUploadService;
  @Mock private GradeCRUDService gradeCRUDService;
  @Mock private SubmissionCRUDService submissionCRUDService;
  @Mock private SubmissionMapper submissionMapper;
  @Mock private TaskMapper taskMapper;
  @Mock private UserValidator userValidator;
  @Mock private TaskValidator taskValidator;

  @InjectMocks private TaskSubmissionFacadeImpl taskSubmissionFacade;

  @Test
  void addSubmissionToTask_NullTaskId_ThrowsException() {
    final var submissionJson = "{\"userId\": 1, \"taskId\": null}";
    final List<MultipartFile> files = List.of(mock(MultipartFile.class));
    String taskId = null;
    final var submissionDto = new SubmissionDto();

    when(submissionMapper.fromJson(submissionJson)).thenReturn(submissionDto);
    when(taskValidator.isTaskExistsInDbById(null)).thenReturn(false);

    final var exception =
        assertThrows(
            TaskNotFoundException.class,
            () -> {
              taskSubmissionFacade.addSubmissionToTask(submissionJson, files, taskId);
            });

    final var expectedMessage = "Task with id: null does not exist.";
    final var actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void addSubmissionToTask_UserDoesNotExist_ThrowsException() {
    final var submissionJson = "{\"userId\": 2, \"taskId\": \"task1\"}";
    final List<MultipartFile> files = List.of(mock(MultipartFile.class));
    final var taskId = "task1";
    final var submissionDto = SubmissionDto.builder().userId(2L).taskId(taskId).build();

    when(submissionMapper.fromJson(submissionJson)).thenReturn(submissionDto);
    when(taskValidator.isTaskExistsInDbById(anyString())).thenReturn(true);
    when(userValidator.isUserExistInDbById(anyLong())).thenReturn(false);

    final var exception =
        assertThrows(
            UserNotFoundException.class,
            () -> {
              taskSubmissionFacade.addSubmissionToTask(submissionJson, files, taskId);
            });

    final var expectedMessage = "User with id: 2 does not exist.";
    final var actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void addSubmissionToTask_FileUploadFails_ThrowsException() {
    final var submissionJson = "{\"userId\": 1, \"taskId\": \"task1\"}";
    final MultipartFile file = mock(MultipartFile.class);
    final List<MultipartFile> files = List.of(file);
    final var taskId = "task1";
    final var submissionDto = SubmissionDto.builder().userId(1L).taskId(taskId).build();

    when(submissionMapper.fromJson(submissionJson)).thenReturn(submissionDto);
    when(taskValidator.isTaskExistsInDbById(anyString())).thenReturn(true);
    when(userValidator.isUserExistInDbById(anyLong())).thenReturn(true);
    when(fileUploadService.uploadFile(file)).thenThrow(new RuntimeException("File upload failed"));

    final var exception =
        assertThrows(
            RuntimeException.class,
            () -> {
              taskSubmissionFacade.addSubmissionToTask(submissionJson, files, taskId);
            });

    final var expectedMessage = "File upload failed";
    final var actualMessage = exception.getMessage();

    assertTrue(actualMessage.contains(expectedMessage));
  }

  @Test
  void addSubmissionToTask_Success() {
    final var submissionJson = "{\"userId\": 1, \"taskId\": \"task1\"}";
    final MultipartFile file = mock(MultipartFile.class);
    final List<MultipartFile> files = List.of(file);
    final var taskId = "task1";
    final var submissionDto = SubmissionDto.builder().userId(1L).taskId(taskId).build();
    final var taskDto = new TaskDto();
    final var task = new Task();
    final var submission = new Submission();
    final var savedSubmission = new Submission();

    when(submissionMapper.fromJson(submissionJson)).thenReturn(submissionDto);
    when(submissionMapper.submissionDtoToSubmission(submissionDto)).thenReturn(submission);
    when(taskCRUDService.getTaskById(taskId)).thenReturn(task).thenReturn(task);
    when(taskMapper.taskToTaskDto(task)).thenReturn(taskDto);
    when(taskValidator.isTaskExistsInDbById(anyString())).thenReturn(true);
    when(userValidator.isUserExistInDbById(anyLong())).thenReturn(true);
    when(fileUploadService.uploadFile(file)).thenReturn("uploadedFileName");
    when(submissionCRUDService.saveSubmission(submission)).thenReturn(savedSubmission);

    final var result = taskSubmissionFacade.addSubmissionToTask(submissionJson, files, taskId);

    assertEquals(taskDto, result);
    verify(submissionMapper).fromJson(submissionJson);
    verify(submissionMapper).submissionDtoToSubmission(submissionDto);
    verify(taskCRUDService, times(2)).getTaskById(taskId);
    verify(taskMapper).taskToTaskDto(task);
    verify(taskValidator).isTaskExistsInDbById(anyString());
    verify(userValidator).isUserExistInDbById(anyLong());
    verify(fileUploadService).uploadFile(file);
    verify(submissionCRUDService).saveSubmission(submission);
  }
}
