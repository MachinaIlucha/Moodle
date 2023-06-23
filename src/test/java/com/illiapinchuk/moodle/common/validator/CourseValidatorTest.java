package com.illiapinchuk.moodle.common.validator;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.illiapinchuk.moodle.persistence.repository.CourseRepository;
import com.illiapinchuk.moodle.persistence.repository.UserRepository;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CourseValidatorTest {

  private static final String EXISTING_COURSE_ID = "existingCourseId";
  private static final String NON_EXISTING_COURSE_ID = "nonExistingCourseId";
  private static final List<String> EXISTING_AUTHOR_IDS = Arrays.asList("1", "2", "3");
  private static final List<String> NON_EXISTING_AUTHOR_IDS = Collections.singletonList("4");

  @Mock
  private CourseRepository courseRepository;

  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private CourseValidator courseValidator;

  @Test
  void isCourseExistsInDbById_existingCourseId_trueReturned() {
    when(courseRepository.existsById(EXISTING_COURSE_ID)).thenReturn(true);

    boolean result = courseValidator.isCourseExistsInDbById(EXISTING_COURSE_ID);

    assertTrue(result);
    verify(courseRepository, times(1)).existsById(EXISTING_COURSE_ID);
  }

  @Test
  void isCourseExistsInDbById_nonExistingCourseId_falseReturned() {
    when(courseRepository.existsById(NON_EXISTING_COURSE_ID)).thenReturn(false);

    boolean result = courseValidator.isCourseExistsInDbById(NON_EXISTING_COURSE_ID);

    assertFalse(result);
    verify(courseRepository, times(1)).existsById(NON_EXISTING_COURSE_ID);
  }

  @Test
  void isAuthorsExistsInDbByIds_allExistingAuthorIds_trueReturned() {
    when(userRepository.existsById(1L)).thenReturn(true);
    when(userRepository.existsById(2L)).thenReturn(true);
    when(userRepository.existsById(3L)).thenReturn(true);

    boolean result = courseValidator.isAuthorsExistsInDbByIds(EXISTING_AUTHOR_IDS);

    assertTrue(result);
    verify(userRepository, times(3)).existsById(anyLong());
  }

  @Test
  void isAuthorsExistsInDbByIds_nonExistingAuthorId_falseReturned() {
    when(userRepository.existsById(4L)).thenReturn(false);

    boolean result = courseValidator.isAuthorsExistsInDbByIds(NON_EXISTING_AUTHOR_IDS);

    assertFalse(result);
    verify(userRepository, times(1)).existsById(anyLong());
  }
}
