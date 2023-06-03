package com.illiapinchuk.moodle.model.dto;

import com.illiapinchuk.moodle.common.ApplicationConstants;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

/**
 * DTO class for authentication (login) request.
 */
@EqualsAndHashCode
@Getter
@ToString
@Builder
public class AuthRequestDto {

  @Size(min = ApplicationConstants.Web.Security.MIN_SIZE_OF_EMAIL,
      max = ApplicationConstants.Web.Security.MAX_SIZE_OF_EMAIL)
  @NotBlank
  private String email;

  @Size(min = ApplicationConstants.Web.Security.MIN_SIZE_OF_PASSWORD,
      max = ApplicationConstants.Web.Security.MAX_SIZE_OF_PASSWORD)
  @NotBlank
  private String password;
}
