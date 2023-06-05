package com.illiapinchuk.moodle.model.dto;

import com.illiapinchuk.moodle.common.constants.ApplicationConstants;
import com.illiapinchuk.moodle.persistence.entity.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Date;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/** Incoming DTO to user creation {@link User}. */
@Data
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@NoArgsConstructor
public class UserCreationDto {
  @Size(
      min = ApplicationConstants.Web.DataValidation.MIN_SIZE_OF_SURNAME,
      max = ApplicationConstants.Web.DataValidation.MAX_SIZE_OF_SURNAME,
      message = ApplicationConstants.Web.DataValidation.ErrorMessage.SURNAME_ERROR_MESSAGE)
  String surname;

  @Size(
      min = ApplicationConstants.Web.DataValidation.MIN_SIZE_OF_LASTNAME,
      max = ApplicationConstants.Web.DataValidation.MAX_SIZE_OF_LASTNAME,
      message = ApplicationConstants.Web.DataValidation.ErrorMessage.LASTNAME_ERROR_MESSAGE)
  String lastname;

  @NotBlank(
      message = ApplicationConstants.Web.DataValidation.ErrorMessage.EMAIL_BLANK_ERROR_MESSAGE)
  @Email(message = ApplicationConstants.Web.DataValidation.ErrorMessage.EMAIL_ERROR_MESSAGE)
  @Size(
      min = ApplicationConstants.Web.DataValidation.MIN_SIZE_OF_EMAIL,
      max = ApplicationConstants.Web.DataValidation.MAX_SIZE_OF_EMAIL,
      message = ApplicationConstants.Web.DataValidation.ErrorMessage.EMAIL_SIZE_ERROR_MESSAGE)
  String email;

  @NotBlank(
      message = ApplicationConstants.Web.DataValidation.ErrorMessage.LOGIN_BLANK_ERROR_MESSAGE)
  @Size(
      min = ApplicationConstants.Web.DataValidation.MIN_SIZE_OF_LOGIN,
      max = ApplicationConstants.Web.DataValidation.MAX_SIZE_OF_LOGIN,
      message = ApplicationConstants.Web.DataValidation.ErrorMessage.LOGIN_SIZE_ERROR_MESSAGE)
  String login;

  @NotBlank(
      message = ApplicationConstants.Web.DataValidation.ErrorMessage.PASSWORD_BLANK_ERROR_MESSAGE)
  @Size(
      min = ApplicationConstants.Web.DataValidation.MIN_SIZE_OF_PASSWORD,
      max = ApplicationConstants.Web.DataValidation.MAX_SIZE_OF_PASSWORD,
      message = ApplicationConstants.Web.DataValidation.ErrorMessage.PASSWORD_SIZE_ERROR_MESSAGE)
  String password;

  @Size(
      min = ApplicationConstants.Web.DataValidation.MIN_SIZE_OF_BIO,
      max = ApplicationConstants.Web.DataValidation.MAX_SIZE_OF_BIO,
      message = ApplicationConstants.Web.DataValidation.ErrorMessage.BIO_SIZE_ERROR_MESSAGE)
  String bio;

  @NotBlank(
      message = ApplicationConstants.Web.DataValidation.ErrorMessage.PHONE_BLANK_ERROR_MESSAGE)
  @Pattern(
      regexp = ApplicationConstants.Validation.PHONE_REGEX,
      message = ApplicationConstants.Web.DataValidation.ErrorMessage.PHONE_PATTERN_ERROR_MESSAGE)
  String phoneNumber;

  @Past(message = ApplicationConstants.Web.DataValidation.ErrorMessage.DATE_BIRTH_ERROR_MESSAGE)
  Date dateOfBirth;

  @Size(
      min = ApplicationConstants.Web.DataValidation.MIN_SIZE_OF_COUNTRY,
      max = ApplicationConstants.Web.DataValidation.MAX_SIZE_OF_COUNTRY,
      message = ApplicationConstants.Web.DataValidation.ErrorMessage.COUNTRY_SIZE_ERROR_MESSAGE)
  String country;

  @Size(
      min = ApplicationConstants.Web.DataValidation.MIN_SIZE_OF_CITY,
      max = ApplicationConstants.Web.DataValidation.MAX_SIZE_OF_CITY,
      message = ApplicationConstants.Web.DataValidation.ErrorMessage.CITY_SIZE_ERROR_MESSAGE)
  String city;
}
