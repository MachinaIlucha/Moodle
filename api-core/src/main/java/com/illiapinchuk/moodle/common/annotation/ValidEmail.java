package com.illiapinchuk.moodle.common.annotation;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.illiapinchuk.moodle.common.validator.EmailValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * The @ValidEmail annotation is used to verify that a given string value is a valid email address.
 * The annotation accepts a `message` attribute, which specifies the error
 * message to be shown if the email address is not valid. It also supports
 * the standard `groups` and `payload` attributes for use in validation
 * groups and payloads.
 * The annotation is used by the EmailValidator class to perform the actual validation.
 */
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE,
    ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
@Documented
public @interface ValidEmail {

  /**
   * Error message.
   */
  String message() default "Email not valid";

  /**
   * Group attribute.
   */
  Class<?>[] groups() default {};

  /**
   * Payload attribute.
   */
  Class<? extends Payload>[] payload() default {};
}
