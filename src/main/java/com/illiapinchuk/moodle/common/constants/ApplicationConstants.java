package com.illiapinchuk.moodle.common.constants;

import java.util.Calendar;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

/** Contains various constants used in the messenger application. */
@UtilityClass
public class ApplicationConstants {

  /** Inner utility class for validation-related constants. */
  @UtilityClass
  public class Validation {
    /** A regular expression for validating phone number. */
    public static final String PHONE_REGEX = "\\+?[0-9]+";
    /** A regular expression for validating email addresses. */
    public static final String EMAIL_REGEX =
        "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    public static final Pattern EMAIL_PATTERN = Pattern.compile(Validation.EMAIL_REGEX);
  }

  /** Inner utility class for web-related constants. */
  @UtilityClass
  public class Web {

    /** Inner utility class for web-related paths. */
    @UtilityClass
    public class Path {
      public static final String LOGIN_PATH = "/auth/login";
    }

    /**
     * Const's for Swagger docs.
     */
    @UtilityClass
    public class Swagger {
      public static final String API_DOCS_PATH = "/v3/api-docs/**";
      public static final String UI_PATH = "/swagger-ui/**";
      public static final String UI_HTML_PATH = "/swagger-ui.html";
    }

    /** Inner utility class for constants related to security part. */
    @UtilityClass
    public class Security {
      public static final String TOKEN_HEADER_NAME = "token";
      public static final String SERVER_TIMEZONE_ID =
          Calendar.getInstance().getTimeZone().toZoneId().toString();

      /** Inner utility class for constants related to security jwt claims part. */
      @UtilityClass
      public class JwtClaims {
        public static final String ROLES = "roles";
        public static final String TIME_ZONE_ID = "timeZoneId";
      }
    }

    /** Inner utility class for dto validation. */
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

      /** Inner utility class for dto validation errors. */
      @UtilityClass
      public class ErrorMessage {
        public static final String SURNAME_ERROR_MESSAGE =
            "Surname may not be longer than 100 characters";
        public static final String LASTNAME_ERROR_MESSAGE =
            "Surname may not be longer than 100 characters";
        public static final String EMAIL_BLANK_ERROR_MESSAGE = "Email is mandatory";
        public static final String EMAIL_ERROR_MESSAGE = "Email should be valid";
        public static final String EMAIL_SIZE_ERROR_MESSAGE =
            "Email may not be longer than 100 characters and not less then 5 characters";
        public static final String LOGIN_BLANK_ERROR_MESSAGE = "Login is mandatory";
        public static final String LOGIN_SIZE_ERROR_MESSAGE =
            "Login should be between 5 and 15 characters";
        public static final String BIO_SIZE_ERROR_MESSAGE =
            "Login should be between 5 and 255 characters";
        public static final String PHONE_BLANK_ERROR_MESSAGE = "Phone is mandatory";
        public static final String PHONE_PATTERN_ERROR_MESSAGE =
            "Phone number can only contain numbers and optionally can start with '+'";
        public static final String DATE_BIRTH_ERROR_MESSAGE = "Date of birth must be in the past";
        public static final String COUNTRY_SIZE_ERROR_MESSAGE =
            "Country should be between 3 and 100 characters";
        public static final String CITY_SIZE_ERROR_MESSAGE =
            "City should be between 3 and 100 characters";
        public static final String PASSWORD_BLANK_ERROR_MESSAGE = "Password is mandatory";
        public static final String PASSWORD_SIZE_ERROR_MESSAGE =
            "City should be between 6 and 255 characters";
      }
    }
  }
}
