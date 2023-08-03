package com.illiapinchuk.moodle.common;

import com.illiapinchuk.moodle.model.dto.AuthRequestDto;
import com.illiapinchuk.moodle.model.dto.CourseDto;
import lombok.experimental.UtilityClass;

import java.util.List;

@UtilityClass
public class TestConstants {

  @UtilityClass
  public class Path {
    public static final String LOGIN_PATH = "/auth/login";
    public static final String USER_CONTROLLER_PATH = "/users";
    public static final String COURSE_WITH_ID_CONTROLLER_PATH = "/courses/{id}";
    public static final String COURSE_CONTROLLER_PATH = "/courses";
    public static final String EXPIRE_JWT_TOKEN_PATH = "/token/expire";
  }

  @UtilityClass
  public class Course {
    public static final String VALID_COURSE_ID = "1";
    public static final String VALID_COURSE_ID_TO_DELETE = "2";
    public static final String INVALID_COURSE_ID = "notValidId";
    public static final CourseDto VALID_COURSE_DTO =
        CourseDto.builder()
            .name("Java Course")
            .description("This is java core course.")
            .authorIds(List.of("2"))
            .build();
    public static final CourseDto VALID_COURSE_DTO_WITH_ID =
        CourseDto.builder()
            .id(VALID_COURSE_ID)
            .name("Java Course with id")
            .description("This is java core course. With id.")
            .authorIds(List.of("1", "2"))
            .build();
    public static final CourseDto VALID_COURSE_DTO_WITH_TWO_AUTHORS =
        CourseDto.builder()
            .name("Java Course")
            .description("This is java core course.")
            .authorIds(List.of("1", "2"))
            .build();
    public static final CourseDto COURSE_DTO_WITHOUT_AUTHORS =
        CourseDto.builder()
            .name("Java Course without authors")
            .description("This is java core course.")
            .build();
    public static final String VALID_COURSE_DTO_AS_STRING =
        """
                                      {
                                          "id": "1",
                                          "name": "Java Programming Course 2",
                                          "description": "A comprehensive course on Java programming 2",
                                          "authorIds": ["1", "2"],
                                          "students": []
                                      }
                                      """;
    public static final String COURSE_DTO_INVALID_ID_AS_STRING =
            """
                                          {
                                              "id": "invalid ID",
                                              "name": "Java Programming Course 2",
                                              "description": "A comprehensive course on Java programming 2",
                                              "authorIds": ["1", "2"],
                                              "students": []
                                          }
                                          """;
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
          AuthRequestDto.builder()
              .loginOrEmail("nonExistingUsername")
              .password("incorrectPassword")
              .build();
      public static final AuthRequestDto NON_EXISTING_USER_EMAIL_AND_PASSWORD_AUTH_DTO =
          AuthRequestDto.builder()
              .loginOrEmail("nonExistingEmail@example.com")
              .password("incorrectPassword")
              .build();
    }
  }
}
