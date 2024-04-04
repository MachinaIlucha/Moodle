package com.illiapinchuk.moodle.common;

import com.illiapinchuk.moodle.model.dto.AuthRequestDto;
import com.illiapinchuk.moodle.model.dto.CourseDto;
import com.illiapinchuk.moodle.model.dto.TaskDto;
import com.illiapinchuk.moodle.model.entity.TaskStatus;
import com.illiapinchuk.moodle.persistence.entity.Course;
import com.illiapinchuk.moodle.persistence.entity.Task;
import lombok.experimental.UtilityClass;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static com.illiapinchuk.moodle.common.TestConstants.CourseConstants.INVALID_COURSE_ID;

@UtilityClass
public class TestConstants {

  @UtilityClass
  public class Path {
    public static final String LOGIN_PATH = "/auth/login";
    public static final String USER_CONTROLLER_PATH = "/users";
    public static final String COURSE_WITH_ID_CONTROLLER_PATH = "/courses/{id}";
    public static final String ADD_STUDENTS_TO_COURSE_CONTROLLER_PATH =
        "/courses/{courseId}/students";
    public static final String REMOVE_STUDENT_FROM_COURSE_CONTROLLER_PATH =
        "/courses/{courseId}/students/{studentId}";
    public static final String GET_STUDENT_COURSES_CONTROLLER_PATH =
        "/courses/student/{studentId}/courses";
    public static final String GET_AUTH_USER_COURSES_CONTROLLER_PATH = "/courses/my-courses";
    public static final String COURSE_CONTROLLER_PATH = "/courses";
    public static final String TASK_CONTROLLER_PATH = "/tasks";
    public static final String TASK_WITH_ID_CONTROLLER_PATH = "/tasks/{id}";
    public static final String TASK_UPLOAD_ATTACHMENT_CONTROLLER_PATH = "/tasks/{id}/upload";
    public static final String EXPIRE_JWT_TOKEN_PATH = "/token/expire";
    public static final String PASSWORD_RESET_INIT_PATH = "/password/forgot";
    public static final String PASSWORD_RESET_PATH = "/password/reset";
  }

  @UtilityClass
  public class UserConstants {
    public static final String EXISTING_ADMIN_EMAIL = "testAdminUser@example.com";
    public static final String NOT_EXISTING_ADMIN_EMAIL = "notExistingEmail@example.com";
    public static final String EXISTING_USER_EMAIL = "testUser@example.com";
    public static final List<Long> LIST_OF_USERS_IDS = List.of(1L, 2L, 3L);
    public static final List<Long> LIST_OF_USERS_WITH_NON_EXISTS_IDS = List.of(1L, 2L, 99L);
    public static final Long EXISTING_USER_ID = 1L;
    public static final Long NOT_EXISTING_USER_ID = 99L;
  }

  @UtilityClass
  public class TaskConstants {
    public static final String VALID_TASK_ID = "1";
    public static final String NOT_EXISTING_TASK_ID = "notExistingId";
    public static final Task VALID_TASK_1 =
        Task.builder()
            .id("1")
            .title("Task 1")
            .description("Complete assignment")
            .dueDate(LocalDateTime.now())
            .creationDate(LocalDateTime.now())
            .course(new Course())
            .authorId(1L)
            .status(TaskStatus.OPEN)
            .attachments(new ArrayList<>())
            .submissionIds(new ArrayList<>())
            .build();
    public static final Task VALID_TASK_2 =
        Task.builder()
            .id("2")
            .title("Task 2")
            .description("Review chapter 5")
            .dueDate(LocalDateTime.now())
            .creationDate(LocalDateTime.now())
            .course(new Course())
            .authorId(1L)
            .status(TaskStatus.OPEN)
            .attachments(new ArrayList<>())
            .submissionIds(new ArrayList<>())
            .build();
    public static final Task VALID_TASK_3 =
        Task.builder()
            .id("3")
            .title("Task 3")
            .description("Prepare presentation")
            .dueDate(LocalDateTime.now())
            .creationDate(LocalDateTime.now())
            .course(new Course())
            .authorId(2L)
            .status(TaskStatus.OPEN)
            .attachments(new ArrayList<>())
            .submissionIds(new ArrayList<>())
            .build();
    public static final TaskDto VALID_TASK_DTO =
        TaskDto.builder()
            .title("Valid task title")
            .description("Valid task description")
            // due date is set to 1 day from now
            .dueDate(LocalDateTime.now().plusDays(1))
            .creationDate(LocalDateTime.now())
            .courseId("1")
            .authorId(1L)
            .status(TaskStatus.OPEN)
            .build();
    public static final TaskDto INVALID_TASK_DTO_NOT_EXISTED_COURSE_ID =
        TaskDto.builder()
            .title("Valid task title")
            .description("Valid task description")
            // due date is set to 1 day from now
            .dueDate(LocalDateTime.now().plusDays(1))
            .creationDate(LocalDateTime.now())
            .courseId(INVALID_COURSE_ID)
            .authorId(1L)
            .status(TaskStatus.OPEN)
            .build();
    public static final TaskDto INVALID_TASK_DTO_NOT_EXISTED_AUTHOR_ID =
        TaskDto.builder()
            .title("Valid task title")
            .description("Valid task description")
            // due date is set to 1 day from now
            .dueDate(LocalDateTime.now().plusDays(1))
            .creationDate(LocalDateTime.now())
            .courseId("1")
            .authorId(123456789L)
            .status(TaskStatus.OPEN)
            .build();
  }

  @UtilityClass
  public class CourseConstants {
    public static final String VALID_COURSE_ID = "1";
    public static final String VALID_COURSE_WITH_STUDENTS_ID = "3";
    public static final String VALID_COURSE_WITH_STUDENTS_ID_FOR_REMOVING_STUDENT = "4";
    public static final String NOT_VALID_COURSE_ID = "99";
    public static final String VALID_COURSE_ID_TO_DELETE = "2";
    public static final String INVALID_COURSE_ID = "notValidId";
    public static final CourseDto VALID_COURSE_DTO =
        CourseDto.builder()
            .name("Java Course")
            .description("This is java core course.")
            .authorIds(Set.of(2L))
            .build();
    public static final CourseDto VALID_COURSE_DTO_WITH_TWO_AUTHORS =
        CourseDto.builder()
            .name("Java Course")
            .description("This is java core course.")
            .authorIds(Set.of(1L, 2L))
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
