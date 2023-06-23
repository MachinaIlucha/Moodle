package com.illiapinchuk.moodle.api.rest.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import com.illiapinchuk.moodle.common.mapper.TaskMapper;
import com.illiapinchuk.moodle.model.dto.TaskDto;
import com.illiapinchuk.moodle.persistence.entity.Task;
import com.illiapinchuk.moodle.service.CourseService;
import com.illiapinchuk.moodle.service.TaskService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@ExtendWith(MockitoExtension.class)
class TaskControllerTest {

  private static final String TASK_ID = "1";

  @Mock private TaskService taskService;
  @Mock private TaskMapper taskMapper;
  @Mock private CourseService courseService;
  @InjectMocks private TaskController taskController;

  @Test
  void testGetTaskById() {
    Task task = Task.builder().id(TASK_ID).build();

    when(taskService.getTaskById(TASK_ID)).thenReturn(task);
    when(taskMapper.taskToTaskDto(task)).thenReturn(new TaskDto());

    ResponseEntity<TaskDto> responseEntity = taskController.getTaskById(TASK_ID);

    assertEquals(200, responseEntity.getStatusCodeValue());
  }

  @Test
  void testCreateTask() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

    TaskDto taskDto = new TaskDto();
    Task task = Task.builder().id(TASK_ID).build();
    when(taskMapper.taskDtoToTask(taskDto)).thenReturn(task);
    when(taskService.createTask(task)).thenReturn(task);
    when(taskMapper.taskToTaskDto(task)).thenReturn(taskDto);

    ResponseEntity<TaskDto> responseEntity = taskController.createTask(taskDto);

    assertEquals(201, responseEntity.getStatusCodeValue());
  }

  @Test
  void testUpdateTask() {
    TaskDto taskDto = new TaskDto();
    Task task = Task.builder().id(TASK_ID).build();
    when(taskService.updateTask(taskDto)).thenReturn(task);
    when(taskMapper.taskToTaskDto(task)).thenReturn(taskDto);

    ResponseEntity<TaskDto> responseEntity = taskController.updateTask(taskDto);

    assertEquals(200, responseEntity.getStatusCodeValue());
  }

  @Test
  void testDeleteTaskById() {
    ResponseEntity<Void> responseEntity = taskController.deleteTaskById(TASK_ID);

    assertEquals(200, responseEntity.getStatusCodeValue());
  }
}
