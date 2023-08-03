package com.illiapinchuk.moodle.common;

import com.illiapinchuk.moodle.model.dto.AuthRequestDto;
import lombok.experimental.UtilityClass;

@UtilityClass
public class TestConstants {

  @UtilityClass
  public class Path {
    public static final String LOGIN_PATH = "/auth/login";
    public static final String USER_CONTROLLER_PATH = "/users";
    public static final String COURSE_GET_BY_ID_CONTROLLER_PATH = "/courses/{id}";
  }

  @UtilityClass
  public class Course {
    public static final String VALID_COURSE_ID = "1";
    public static final String INVALID_COURSE_ID = "notValidId";
  }

  @UtilityClass
  public class Dto {

    @UtilityClass
    public class Auth {
      public static final AuthRequestDto EXISTING_ADMIN_AUTH_DTO =
              AuthRequestDto.builder().loginOrEmail("testAdminUser").password("123456").build();
      public static final AuthRequestDto EXISTING_USER_LOGIN_AUTH_DTO =
          AuthRequestDto.builder().loginOrEmail("testUser").password("123456").build();
      public static final AuthRequestDto EXISTING_USER_EMAIL_AUTH_DTO =
          AuthRequestDto.builder().loginOrEmail("testUser@example.com").password("123456").build();
      public static final AuthRequestDto NON_EXISTING_USER_LOGIN_AND_PASSWORD_AUTH_DTO =
              AuthRequestDto.builder().loginOrEmail("nonExistingUsername").password("incorrectPassword").build();
      public static final AuthRequestDto NON_EXISTING_USER_EMAIL_AND_PASSWORD_AUTH_DTO =
              AuthRequestDto.builder().loginOrEmail("nonExistingEmail@example.com").password("incorrectPassword").build();
    }
  }
}
