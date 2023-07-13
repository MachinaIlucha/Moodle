package com.illiapinchuk.moodle.common.validator;

import com.illiapinchuk.moodle.persistence.repository.CourseRepository;
import com.illiapinchuk.moodle.persistence.repository.UserRepository;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/** Validation for course-related information. */
@Component
@RequiredArgsConstructor
public class CourseValidator {

  private final CourseRepository courseRepository;
  private final UserRepository userRepository;

  /**
   * Check if the given course exists in the database.
   *
   * @param id the id to check
   * @return true if the course with this id exists in the database, false otherwise
   */
  public boolean isCourseExistsInDbById(@Nonnull final String id) {
    return courseRepository.existsById(id);
  }

  /**
   * Checks if all the authors with the specified IDs exist in the database.
   *
   * @param authorIds a list of non-blank strings representing author IDs
   * @return true if all authors with the given IDs exist in the database, false otherwise
   */
  public boolean isAuthorsExistsInDbByIds(@Nonnull final List<@NotBlank String> authorIds) {
    return authorIds.stream().allMatch(id -> userRepository.existsById(Long.valueOf(id)));
  }
}
