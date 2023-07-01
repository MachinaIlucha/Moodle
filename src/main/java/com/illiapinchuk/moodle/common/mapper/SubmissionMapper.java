package com.illiapinchuk.moodle.common.mapper;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.illiapinchuk.moodle.exception.CannotReadJsonException;
import com.illiapinchuk.moodle.model.dto.SubmissionDto;
import com.illiapinchuk.moodle.persistence.entity.Submission;
import jakarta.validation.constraints.NotNull;
import java.io.IOException;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * This interface defines methods for mapping between the {@link Submission} and {@link
 * SubmissionDto}.
 */
@Mapper(componentModel = "spring")
public interface SubmissionMapper {

  ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  /**
   * Maps a {@link Submission} object to a {@link SubmissionDto} object.
   *
   * @param submission the submission object to be mapped
   * @return the corresponding submission DTO object
   */
  @Mapping(target = "submissionFiles", ignore = true)
  SubmissionDto submissionToSubmissionDto(Submission submission);

  /**
   * Maps a {@link SubmissionDto} object to a {@link Submission} object.
   *
   * @param submissionDto the submission DTO object to be mapped
   * @return the corresponding submission object
   */
  @Mapping(target = "submissionFiles", ignore = true)
  Submission submissionDtoToSubmission(SubmissionDto submissionDto);

  /**
   * Converts the provided JSON string to a {@link SubmissionDto} object.
   *
   * @param json the JSON string to be converted
   * @return the corresponding {@link SubmissionDto} object
   * @throws CannotReadJsonException if there is an error reading the JSON data
   */
  default SubmissionDto fromJson(@NotNull final String json) {
    try {
      return OBJECT_MAPPER.readValue(json, SubmissionDto.class);
    } catch (IOException ioException) {
      throw new CannotReadJsonException(
          String.format("Failed to read JSON data from the provided string: %s", json));
    }
  }
}
