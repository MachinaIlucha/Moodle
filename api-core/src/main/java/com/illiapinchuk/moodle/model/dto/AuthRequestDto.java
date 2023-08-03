package com.illiapinchuk.moodle.model.dto;

import com.illiapinchuk.moodle.common.ApplicationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/** DTO class for authentication (login) request. */
@EqualsAndHashCode
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthRequestDto {

  @Size(
      min = ApplicationConstants.Web.DataValidation.MIN_SIZE_OF_EMAIL,
      max = ApplicationConstants.Web.DataValidation.MAX_SIZE_OF_EMAIL)
  @NotBlank
  private String loginOrEmail;

  @Size(
      min = ApplicationConstants.Web.DataValidation.MIN_SIZE_OF_PASSWORD,
      max = ApplicationConstants.Web.DataValidation.MAX_SIZE_OF_PASSWORD)
  @NotBlank
  private String password;
}
