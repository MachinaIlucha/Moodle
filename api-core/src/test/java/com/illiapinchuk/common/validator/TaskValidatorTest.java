package com.illiapinchuk.common.validator;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.illiapinchuk.common.TestConstants;
import com.illiapinchuk.moodle.common.validator.TaskValidator;
import com.illiapinchuk.moodle.persistence.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class TaskValidatorTest {

  @Mock private TaskRepository taskRepository;

  @InjectMocks private TaskValidator taskValidator;

  @Test
  void testIsTaskExistsInDbById_WithExistingId_ReturnsTrue() {
    when(taskRepository.existsById(TestConstants.TaskConstants.TASK_ID)).thenReturn(true);

    boolean result = taskValidator.isTaskExistsInDbById(TestConstants.TaskConstants.TASK_ID);

    assertTrue(result);
    verify(taskRepository).existsById(TestConstants.TaskConstants.TASK_ID);
  }

  @Test
  void testIsTaskExistsInDbById_WithNonExistingId_ReturnsFalse() {
    when(taskRepository.existsById(TestConstants.TaskConstants.TASK_ID)).thenReturn(false);

    boolean result = taskValidator.isTaskExistsInDbById(TestConstants.TaskConstants.TASK_ID);

    assertFalse(result);
    verify(taskRepository).existsById(TestConstants.TaskConstants.TASK_ID);
  }
}
