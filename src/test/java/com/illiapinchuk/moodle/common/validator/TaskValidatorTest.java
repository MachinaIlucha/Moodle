package com.illiapinchuk.moodle.common.validator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import com.illiapinchuk.moodle.persistence.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskValidatorTest {

  private static final String EXISTING_TASK_ID = "task123";
  private static final String NON_EXISTING_TASK_ID = "task456";

  @Mock
  private TaskRepository taskRepository;

  @InjectMocks
  private TaskValidator taskValidator;

  @Test
  void testIsTaskExistsInDbById_TaskExists() {
    when(taskRepository.existsById(EXISTING_TASK_ID)).thenReturn(true);

    boolean result = taskValidator.isTaskExistsInDbById(EXISTING_TASK_ID);

    assertTrue(result);
    verify(taskRepository).existsById(EXISTING_TASK_ID);
  }

  @Test
  void testIsTaskExistsInDbById_TaskDoesNotExist() {
    when(taskRepository.existsById(NON_EXISTING_TASK_ID)).thenReturn(false);

    boolean result = taskValidator.isTaskExistsInDbById(NON_EXISTING_TASK_ID);

    assertFalse(result);
    verify(taskRepository).existsById(NON_EXISTING_TASK_ID);
  }
}
