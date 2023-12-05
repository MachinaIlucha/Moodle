package com.illiapinchuk.moodle.common.mapper;

import com.illiapinchuk.moodle.model.dto.GradeDto;
import com.illiapinchuk.moodle.persistence.entity.Grade;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

/** This interface defines methods for mapping between the {@link Grade} and {@link GradeDto}. */
@Mapper(componentModel = "spring")
public interface GradeMapper {
  GradeMapper INSTANCE = Mappers.getMapper(GradeMapper.class);

  /**
   * Maps a {@link Grade} object to a {@link GradeDto} object.
   *
   * @param grade The {@link Grade} object to be mapped.
   * @return The resulting {@link GradeDto} object.
   */
  GradeDto gradeToGradeDto(Grade grade);

  /**
   * Maps a {@link GradeDto} object to a {@link Grade} object.
   *
   * @param gradeDto The {@link GradeDto} object to be mapped.
   * @return The resulting {@link Grade} object.
   */
  Grade gradeDtoToGrade(GradeDto gradeDto);

  /**
   * This method updates the {@link Grade} object with the data from the {@link GradeDto}.
   *
   * @param grade The Grade object to be updated.
   * @param gradeDto The source of the updated data.
   */
  void updateGrade(@MappingTarget Grade grade, GradeDto gradeDto);
}
