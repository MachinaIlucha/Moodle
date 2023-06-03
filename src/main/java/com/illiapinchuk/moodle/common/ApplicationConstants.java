package com.illiapinchuk.moodle.common;

import java.util.Calendar;
import lombok.experimental.UtilityClass;

/**
 * Contains various constants used in the messenger application.
 */
@UtilityClass
public class ApplicationConstants {

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
    }

    /**
     * Inner utility class for constants related to security part.
     */
    @UtilityClass
    public class Security {
      public static final String TOKEN_HEADER_NAME = "Authorization";
      public static final int MAX_SIZE_OF_EMAIL = 100;
      public static final int MIN_SIZE_OF_EMAIL = 5;
      public static final int MAX_SIZE_OF_PASSWORD = 255;
      public static final int MIN_SIZE_OF_PASSWORD = 6;

      public static final String SERVER_TIMEZONE_ID =
          Calendar.getInstance().getTimeZone().toZoneId().toString();

      /**
       * Inner utility class for constants related to security jwt claims part.
       */
      @UtilityClass
      public class JwtClaims {
        public static final String ROLES = "roles";
        public static final String TIME_ZONE_ID = "timeZoneId";
      }
    }
  }
}
