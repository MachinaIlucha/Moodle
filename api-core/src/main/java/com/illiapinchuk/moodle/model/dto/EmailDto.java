package com.illiapinchuk.moodle.model.dto;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/** DTO class for creating email letters. */
@EqualsAndHashCode
@Getter
@ToString
@Builder
public class EmailDto {
  private String to;
  private String subject;
  private String content;
}
