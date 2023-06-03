package com.illiapinchuk.moodle.common;

import lombok.experimental.UtilityClass;

/**
 * Contains various constants used in the messenger application.
 */
@UtilityClass
public class ApplicationConstants {

  /**
   * Inner utility class for validation-related constants.
   */
  @UtilityClass
  public class Validation {
    /**
     * A regular expression for validating phone number.
     */
    public static final String PHONE_REGEX = "\\+?[0-9]+";
  }

  /**
   * Inner utility class for web-related constants.
   */
  @UtilityClass
  public class Web {

    /**
     * Inner utility class for dto validation.
     */
    @UtilityClass
    public class DataValidation {
      public static final int MIN_SIZE_OF_PASSWORD = 6;
      public static final int MAX_SIZE_OF_PASSWORD = 255;
      public static final int MIN_SIZE_OF_LOGIN = 5;
      public static final int MAX_SIZE_OF_LOGIN = 50;
      public static final int MIN_SIZE_OF_EMAIL = 5;
      public static final int MAX_SIZE_OF_EMAIL = 100;
      public static final int MIN_SIZE_OF_SURNAME = 5;
      public static final int MAX_SIZE_OF_SURNAME = 255;
      public static final int MIN_SIZE_OF_LASTNAME = 5;
      public static final int MAX_SIZE_OF_LASTNAME = 255;
      public static final int MIN_SIZE_OF_BIO = 5;
      public static final int MAX_SIZE_OF_BIO = 255;
      public static final int MIN_SIZE_OF_COUNTRY = 3;
      public static final int MAX_SIZE_OF_COUNTRY = 100;
      public static final int MIN_SIZE_OF_CITY = 3;
      public static final int MAX_SIZE_OF_CITY = 100;

      /**
       * Inner utility class for dto validation errors.
       */
      @UtilityClass
      public class ErrorMessage {
        public static final String SURNAME_ERROR_MESSAGE = "Surname may not be longer than 100 characters";
        public static final String LASTNAME_ERROR_MESSAGE = "Surname may not be longer than 100 characters";
        public static final String EMAIL_BLANK_ERROR_MESSAGE = "Email is mandatory";
        public static final String EMAIL_ERROR_MESSAGE = "Email should be valid";
        public static final String EMAIL_SIZE_ERROR_MESSAGE = "Email may not be longer than 100 characters and not less then 5 characters";
        public static final String LOGIN_BLANK_ERROR_MESSAGE = "Login is mandatory";
        public static final String LOGIN_SIZE_ERROR_MESSAGE = "Login should be between 5 and 15 characters";
        public static final String BIO_SIZE_ERROR_MESSAGE = "Login should be between 5 and 255 characters";
        public static final String PHONE_BLANK_ERROR_MESSAGE = "Phone is mandatory";
        public static final String PHONE_PATTERN_ERROR_MESSAGE = "Phone number can only contain numbers and optionally can start with '+'";
        public static final String DATE_BIRTH_ERROR_MESSAGE = "Date of birth must be in the past";
        public static final String COUNTRY_SIZE_ERROR_MESSAGE = "Country should be between 3 and 100 characters";
        public static final String CITY_SIZE_ERROR_MESSAGE = "City should be between 3 and 100 characters";
        public static final String PASSWORD_BLANK_ERROR_MESSAGE = "Password is mandatory";
        public static final String PASSWORD_SIZE_ERROR_MESSAGE = "City should be between 6 and 255 characters";
      }
    }
  }
}
