package com.illiapinchuk.moodle.service.impl;

import com.illiapinchuk.moodle.common.date.DateService;
import com.illiapinchuk.moodle.common.mapper.TaskMapper;
import com.illiapinchuk.moodle.common.validator.TaskValidator;
import com.illiapinchuk.moodle.exception.TaskNotFoundException;
import com.illiapinchuk.moodle.model.dto.TaskDto;
import com.illiapinchuk.moodle.persistence.entity.Task;
import com.illiapinchuk.moodle.persistence.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.Optional;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

  private static final String EXISTING_TASK_ID = "1";
  private static final String NON_EXISTING_TASK_ID = "2";

  @Mock
  private TaskRepository taskRepository;

  @Mock
  private TaskMapper taskMapper;

  @Mock
  private TaskValidator taskValidator;

  @Mock
  private DateService dateService;

  @InjectMocks
  private TaskServiceImpl taskService;

  @Test
  void getTaskById_ExistingId_ReturnsTask() {
    Task task = new Task();
    when(taskRepository.findById(EXISTING_TASK_ID)).thenReturn(Optional.of(task));

    Task result = taskService.getTaskById(EXISTING_TASK_ID);

    assertNotNull(result);
    assertEquals(task, result);
    verify(taskRepository, times(1)).findById(EXISTING_TASK_ID);
  }

  @Test
  void getTaskById_NonExistingId_ThrowsTaskNotFoundException() {
    when(taskRepository.findById(NON_EXISTING_TASK_ID)).thenReturn(Optional.empty());

    assertThrows(TaskNotFoundException.class, () -> taskService.getTaskById(NON_EXISTING_TASK_ID));
    verify(taskRepository, times(1)).findById(NON_EXISTING_TASK_ID);
  }

  @Test
  void testCreateTask() {
    Task task = new Task();
    when(taskRepository.save(any(Task.class))).thenReturn(task);

    Task result = taskService.createTask(new Task());

    assertEquals(task, result);
  }

  @Test
  void updateTask_ExistingTaskId_Success() {
    TaskDto taskDto = TaskDto.builder().id(EXISTING_TASK_ID).build();

    Task existingTask = Task.builder().id(EXISTING_TASK_ID).build();
    when(taskValidator.isTaskExistsInDbById(taskDto.getId())).thenReturn(true);
    when(taskRepository.findById(taskDto.getId())).thenReturn(Optional.ofNullable(existingTask));
    when(taskRepository.save(any(Task.class))).thenReturn(existingTask);

    Task result = taskService.updateTask(taskDto);

    assertNotNull(result);
    verify(taskMapper).updateTask(any(Task.class), eq(taskDto));
    verify(taskRepository).save(any(Task.class));
  }

  @Test
  void updateTask_NonExistingTaskId_TaskNotFoundException() {
    TaskDto taskDto = new TaskDto();
    taskDto.setId(NON_EXISTING_TASK_ID);

    when(taskValidator.isTaskExistsInDbById(taskDto.getId())).thenReturn(false);

    assertThrows(TaskNotFoundException.class, () -> taskService.updateTask(taskDto));
    verify(taskMapper, never()).updateTask(any(Task.class), eq(taskDto));
    verify(taskRepository, never()).save(any(Task.class));
  }

  @Test
  void testDeleteTaskById() {
    doNothing().when(taskRepository).deleteById(anyString());

    assertDoesNotThrow(() -> taskService.deleteTaskById(EXISTING_TASK_ID));
  }
}