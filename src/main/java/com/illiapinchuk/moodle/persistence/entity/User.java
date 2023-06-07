package com.illiapinchuk.moodle.persistence.entity;

import com.illiapinchuk.moodle.common.constants.ApplicationConstants;
import com.illiapinchuk.moodle.model.entity.RoleName;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.Date;
import java.util.Set;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/** User class represents a user in the db. */
@Entity
@Table(name = "users")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  Long id;

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

  @ElementCollection(targetClass = RoleName.class, fetch = FetchType.EAGER)
  @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"))
  @Column(name = "user_role", nullable = false)
  @Enumerated(EnumType.STRING)
  Set<RoleName> roles;
}
