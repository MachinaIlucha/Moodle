package com.illiapinchuk.moodle.service.impl.crud;

import com.illiapinchuk.moodle.persistence.entity.Grade;
import com.illiapinchuk.moodle.persistence.repository.GradeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.illiapinchuk.moodle.common.TestConstants.TaskConstants.TASK_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class GradeCRUDServiceImplTest {
  @Mock private GradeRepository gradeRepository;

  @InjectMocks private GradeCRUDServiceImpl gradeService;

  @Test
  void saveGrade_ValidGrade_SavesAndReturnsGrade() {
    final var grade = Grade.builder().id(TASK_ID).build();

    when(gradeRepository.save(grade)).thenReturn(grade);

    final var savedGrade = gradeService.saveGrade(grade);

    assertEquals(grade, savedGrade);
  }
}
