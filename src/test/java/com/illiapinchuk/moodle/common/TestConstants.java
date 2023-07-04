package com.illiapinchuk.moodle.common;

import com.illiapinchuk.moodle.model.dto.AuthRequestDto;
import com.illiapinchuk.moodle.model.dto.CourseDto;
import com.illiapinchuk.moodle.model.dto.TaskDto;
import com.illiapinchuk.moodle.model.dto.UserDto;
import com.illiapinchuk.moodle.model.entity.RoleName;
import com.illiapinchuk.moodle.model.entity.TaskStatus;
import com.illiapinchuk.moodle.persistence.entity.Course;
import com.illiapinchuk.moodle.persistence.entity.Task;
import com.illiapinchuk.moodle.persistence.entity.TaskAttachment;
import com.illiapinchuk.moodle.persistence.entity.User;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import lombok.experimental.UtilityClass;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

/** Test constants. */
@UtilityClass
public class TestConstants {

  @UtilityClass
  public class UserConstants {
    public static final String USER_ID = "1";
    public static final String USER_LOGIN = "test-login";
    public static final String USER_EMAIL = "moodle@example.com";
    public static final String USER_INVALID_EMAIL = "invalid.email";
    public static final String USER_VALID_PASSWORD = "valid.password";
    public static final String USER_NULL_EMAIL = null;
    public static final String USER_EMPTY_EMAIL = "";
    public static final String USER_EMAIL_WITH_WHITESPACE = "  " + USER_EMAIL;
    public static final List<String> LIST_OF_USER_IDS = List.of("1", "2", "3");
    public static final List<String> EMPTY_LIST_OF_USER_IDS = Collections.emptyList();

    public static final User VALID_USER =
        User.builder()
            .surname("Smith")
            .lastname("John")
            .email(USER_EMAIL)
            .login(USER_LOGIN)
            .password(USER_VALID_PASSWORD)
            .bio("I am a software engineer.")
            .phoneNumber("+1234567890")
            .dateOfBirth(new Date())
            .country("USA")
            .city("New York")
            .roles(Collections.singleton(RoleName.USER))
            .build();
    public static final UserDto VALID_USER_DTO =
        UserDto.builder()
            .surname("Smith")
            .lastname("John")
            .email(USER_EMAIL)
            .login(USER_LOGIN)
            .password(USER_VALID_PASSWORD)
            .bio("I am a software engineer.")
            .phoneNumber("+1234567890")
            .dateOfBirth(new Date())
            .country("USA")
            .city("New York")
            .build();
  }

  @UtilityClass
  public class TaskConstants {
    public static final String TASK_ID = "1";
    public static final Task VALID_TASK_1 =
        Task.builder()
            .id(TASK_ID)
            .title("Task 1")
            .description("Complete assignment")
            .dueDate(new Date()) // Set the due date to the current date
            .creationDate(new Date()) // Set the creation date to the current date
            .course(new Course()) // Set the course object accordingly
            .authorId("author1")
            .status(TaskStatus.OPEN) // Set the task status accordingly
            .attachments(new ArrayList<>()) // Add any attachments if necessary
            .submissions(new ArrayList<>()) // Add any submissions if necessary
            .build();
    public static final Task VALID_TASK_2 =
        Task.builder()
            .title("Task 2")
            .description("Review chapter 5")
            .dueDate(new Date()) // Set the due date to the current date
            .creationDate(new Date()) // Set the creation date to the current date
            .course(new Course()) // Set the course object accordingly
            .authorId("author2")
            .status(TaskStatus.OPEN) // Set the task status accordingly
            .attachments(new ArrayList<>()) // Add any attachments if necessary
            .submissions(new ArrayList<>()) // Add any submissions if necessary
            .build();
    public static final Task VALID_TASK_3 =
        Task.builder()
            .title("Task 3")
            .description("Prepare presentation")
            .dueDate(new Date()) // Set the due date to the current date
            .creationDate(new Date()) // Set the creation date to the current date
            .course(new Course()) // Set the course object accordingly
            .authorId("author3")
            .status(TaskStatus.OPEN) // Set the task status accordingly
            .attachments(new ArrayList<>()) // Add any attachments if necessary
            .submissions(new ArrayList<>()) // Add any submissions if necessary
            .build();
    public static final List<Task> LIST_OF_VALID_TASKS =
        List.of(VALID_TASK_1, VALID_TASK_2, VALID_TASK_3);
    public static final TaskDto VALID_TASK_DTO =
        TaskDto.builder()
            .id(TASK_ID)
            .title("Sample Task")
            .description("This is a sample task")
            .dueDate(new Date())
            .creationDate(new Date())
            .courseId("course123")
            .authorId("author123")
            .status(TaskStatus.OPEN)
            .build();
  }

  @UtilityClass
  public class TaskAttachmentConstants {
    public static final TaskAttachment VALID_ATTACHMENT =
        TaskAttachment.builder().taskId("1").fileName("attachment1.txt").build();
    public static final TaskAttachment VALID_ATTACHMENT_2 =
        TaskAttachment.builder().taskId("2").fileName("attachment2.txt").build();
    public static final TaskAttachment VALID_ATTACHMENT_3 =
        TaskAttachment.builder().taskId("3").fileName("attachment3.txt").build();
    public static final List<TaskAttachment> LIST_OF_VALID_ATTACHMENTS =
        List.of(VALID_ATTACHMENT, VALID_ATTACHMENT_2, VALID_ATTACHMENT_3);
  }

  @UtilityClass
  public class CourseConstants {
    public static final String VALID_COURSE_ID = "1";
    public static final String INVALID_COURSE_ID = "invalid.courseId";
    public static final String COURSE_NAME = "Java Course";
    public static final String COURSE_DESCRIPTION = "This is java core course.";
    public static final Course VALID_COURSE =
        Course.builder()
            .id(VALID_COURSE_ID)
            .name(COURSE_NAME)
            .description(COURSE_DESCRIPTION)
            .tasks(TaskConstants.LIST_OF_VALID_TASKS)
            .build();
    public static final Course INVALID_COURSE =
        Course.builder()
            .id(INVALID_COURSE_ID)
            .name(COURSE_NAME)
            .description(COURSE_DESCRIPTION)
            .build();
    public static final CourseDto VALID_COURSE_DTO =
        CourseDto.builder()
            .id(VALID_COURSE_ID)
            .name(COURSE_NAME)
            .description(COURSE_DESCRIPTION)
            .build();
    public static final CourseDto INVALID_COURSE_DTO =
        CourseDto.builder()
            .id(INVALID_COURSE_ID)
            .name(COURSE_NAME)
            .description(COURSE_DESCRIPTION)
            .build();
  }

  @UtilityClass
  public class AuthConstants {
    public static final AuthRequestDto AUTH_REQUEST_DTO =
        AuthRequestDto.builder()
            .loginOrEmail(UserConstants.USER_EMAIL)
            .password(UserConstants.USER_VALID_PASSWORD)
            .build();

    public static final String VALID_TOKEN =
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIkFETUlOIl0sInRpbWVab25lSWQiOiJFdXJvcGUvS2lldiIsImlhdCI6MTY4ODIwOTg3MCwiZXhwIjoxNjg4MjQ1ODcwfQ.d6VgCzzJIWo0FzBF82OeV9lchyDYBXYM4hRgiBSIUHM";

    public static final String INVALID_TOKEN =
        "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJhZG1pbiIsInJvbGVzIjpbIkFETUlOIl0sInRpbWVab25lSWQiOiJFdXJvcGUvS2lldiIsImlhdCI6MTY4ODIwOTg3MCwiZXhwIjoxNjg4MjQ1ODcwfQ.d6VgCzzJIWo0FzBF82OeV9lchyDYBXYM4hRgiBSIUHM";
  }

  @UtilityClass
  public class FileConstants {
    public static final String VALID_FILENAME = "example_valid.jpg";
    public static final String VALID_NAME = "image";
    public static final MultipartFile VALID_FILE =
        new MockMultipartFile(VALID_NAME, VALID_FILENAME, "image/jpeg", new byte[] {1, 2, 3});
  }
}
