package com.illiapinchuk.moodle.common;

import java.util.Calendar;
import java.util.regex.Pattern;
import lombok.experimental.UtilityClass;

/** Contains various constants used in the moodle application. */
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

    /** Inner utility class for constants related to resetting passwords. */
    @UtilityClass
    public class PasswordRecovery {
      public static final String RESET_PASSWORD_LINK = "/api/password/reset?token=";
      public static final String LETTER_SUBJECT = "Password reset";
      public static final String RESET_PASSWORD_LETTER =
          """
        Hi there,

        We have received a request to reset the password for your account.
        If you did not make this request, please ignore this email.

        To reset your password, please click on the following link: %s .
        If the link is not working, you can also copy and paste it into your browser's address bar

        Once you have reset your password,
        we recommend that you keep it in a safe place and do not share it with anyone.
        If you have any questions, please don't hesitate to contact our support team.

        Best regards,
        Support team

        P.S. This is an automated message. Please do not reply to this email.
        """;
    }

    /** Inner utility class for metrics constants. */
    @UtilityClass
    public class Metrics {

      /** Inner utility class for metrics tags. */
      @UtilityClass
      public class Tag {
        public static final String REQUEST_URI_TAG = "request_uri";
        public static final String REQUEST_REPOSITORY_METHOD_NAME_TAG = "repository_method_name";
      }

      // Constants for metric names and descriptions
      public static final String APP_REQUESTS_TOTAL = "moodle_requests_total";
      public static final String TOTAL_REQUESTS_DESC = "Total number of HTTP requests";
      public static final String APP_REQUEST_DURATION = "moodle_request_duration";
      public static final String REQUEST_DURATION_DESC = "Duration of HTTP requests";
      public static final String DB_QUERY_DURATION = "moodle_db_query_duration";
      public static final String QUERY_DURATION_DESC = "Duration of database queries";
    }

    /** Inner utility class for web-related paths. */
    @UtilityClass
    public class Path {
      public static final String LOGIN_PATH = "/auth/login";

      /** Inner utility class for actuator paths. */
      @UtilityClass
      public class Actuator {
        public static final String EXTENDED_MAIN_PATH = "/actuator/**";
      }
    }

    /** Const's for Swagger docs. */
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
      public static final int MIN_SIZE_OF_TASK_TITLE = 5;
      public static final int MAX_SIZE_OF_TASK_TITLE = 255;
      public static final int MIN_SIZE_OF_TASK_DESCRIPTION = 5;
      public static final int MAX_SIZE_OF_TASK_DESCRIPTION = 255;
      public static final int MIN_SIZE_OF_COURSE_NAME = 5;
      public static final int MAX_SIZE_OF_COURSE_NAME = 200;
      public static final int MIN_SIZE_OF_COURSE_DESCRIPTION = 1;
      public static final int MAX_SIZE_OF_COURSE_DESCRIPTION = 2000;
      public static final int MAX_SIZE_OF_TASK_GRADE = 10;
      public static final int MAX_SIZE_OF_TASK_ANSWER = 1000;

      /** Inner utility class for dto validation errors. */
      @UtilityClass
      public class ErrorMessage {
        public static final String SURNAME_ERROR_MESSAGE =
            "Surname may not be longer than 100 characters";
        public static final String LASTNAME_ERROR_MESSAGE =
            "Surname may not be longer than 100 characters";
        public static final String TASK_USERID_BLANK_ERROR_MESSAGE = "User ID cannot be blank";
        public static final String TASK_USERID_NULL_ERROR_MESSAGE = "User ID cannot be null";

        public static final String SUBMISSION_FILENAME_BLANK_ERROR_MESSAGE =
            "Filename cannot be blank";
        public static final String SUBMISSION_FILENAME_NULL_ERROR_MESSAGE =
            "Filename cannot be null";
        public static final String TASK_TASKID_BLANK_ERROR_MESSAGE = "Task ID cannot be blank";
        public static final String TASK_TASKID_NULL_ERROR_MESSAGE = "Task ID cannot be null";
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
        public static final String TASK_TITLE_BLANK_ERROR_MESSAGE = "Title is mandatory";
        public static final String TASK_DESCRIPTION_BLANK_ERROR_MESSAGE =
            "Description is mandatory";
        public static final String TASK_COURSE_BLANK_ERROR_MESSAGE = "Course is mandatory";
        public static final String TASK_AUTHOR_BLANK_ERROR_MESSAGE = "Author is mandatory";
        public static final String TASK_STATUS_BLANK_ERROR_MESSAGE = "Status is mandatory";
        public static final String TASK_DESCRIPTION_SIZE_ERROR_MESSAGE =
            "Description should have between 1 and 255 characters";
        public static final String TASK_TITLE_SIZE_ERROR_MESSAGE =
            "Title should have between 1 and 200 characters";
        public static final String TASK_DUEDATE_ERROR_MESSAGE =
            "Due Date must be in the present or future";
        public static final String TASK_CREATIONDATE_ERROR_MESSAGE =
            "Creation Date must be in the past or present";
        public static final String COURSE_NAME_BLANK_ERROR_MESSAGE = "Name is mandatory";
        public static final String COURSE_NAME_SIZE_ERROR_MESSAGE =
            "Name should have between 1 and 200 characters";
        public static final String COURSE_DESCRIPTION_BLANK_ERROR_MESSAGE =
            "Description is mandatory";
        public static final String COURSE_DESCRIPTION_SIZE_ERROR_MESSAGE =
            "Description should have between 1 and 2000 characters";
        public static final String COURSE_AUTHORS_EMPTY_ERROR_MESSAGE =
            "There must be at least one author";
        public static final String TASK_GRADE_SIZE_ERROR_MESSAGE =
            "Grade should be between 0 and 10";
        public static final String TASK_ANSWER_SIZE_ERROR_MESSAGE =
            "Answer should less then 1000 characters!";
      }
    }
  }
}
