package com.illiapinchuk.moodle;

import com.illiapinchuk.moodle.config.MongoInsertData;
import com.illiapinchuk.moodle.config.RedisTestConfiguration;
import com.illiapinchuk.moodle.model.dto.CourseDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Map;

import static com.illiapinchuk.moodle.common.TestConstants.CourseConstants.COURSE_DTO_INVALID_ID_AS_STRING;
import static com.illiapinchuk.moodle.common.TestConstants.CourseConstants.COURSE_DTO_WITHOUT_AUTHORS;
import static com.illiapinchuk.moodle.common.TestConstants.CourseConstants.INVALID_COURSE_ID;
import static com.illiapinchuk.moodle.common.TestConstants.CourseConstants.NOT_VALID_COURSE_ID;
import static com.illiapinchuk.moodle.common.TestConstants.CourseConstants.VALID_COURSE_DTO;
import static com.illiapinchuk.moodle.common.TestConstants.CourseConstants.VALID_COURSE_DTO_AS_STRING;
import static com.illiapinchuk.moodle.common.TestConstants.CourseConstants.VALID_COURSE_DTO_WITH_TWO_AUTHORS;
import static com.illiapinchuk.moodle.common.TestConstants.CourseConstants.VALID_COURSE_ID;
import static com.illiapinchuk.moodle.common.TestConstants.CourseConstants.VALID_COURSE_ID_TO_DELETE;
import static com.illiapinchuk.moodle.common.TestConstants.CourseConstants.VALID_COURSE_WITH_STUDENTS_ID;
import static com.illiapinchuk.moodle.common.TestConstants.CourseConstants.VALID_COURSE_WITH_STUDENTS_ID_FOR_REMOVING_STUDENT;
import static com.illiapinchuk.moodle.common.TestConstants.Dto.Auth.EXISTING_ADMIN_AUTH_DTO;
import static com.illiapinchuk.moodle.common.TestConstants.Dto.Auth.EXISTING_USER_LOGIN_AUTH_DTO;
import static com.illiapinchuk.moodle.common.TestConstants.Path.ADD_STUDENTS_TO_COURSE_CONTROLLER_PATH;
import static com.illiapinchuk.moodle.common.TestConstants.Path.COURSE_CONTROLLER_PATH;
import static com.illiapinchuk.moodle.common.TestConstants.Path.COURSE_WITH_ID_CONTROLLER_PATH;
import static com.illiapinchuk.moodle.common.TestConstants.Path.GET_AUTH_USER_COURSES_CONTROLLER_PATH;
import static com.illiapinchuk.moodle.common.TestConstants.Path.GET_STUDENT_COURSES_CONTROLLER_PATH;
import static com.illiapinchuk.moodle.common.TestConstants.Path.LOGIN_PATH;
import static com.illiapinchuk.moodle.common.TestConstants.Path.REMOVE_STUDENT_FROM_COURSE_CONTROLLER_PATH;
import static com.illiapinchuk.moodle.common.TestConstants.UserConstants.EXISTING_USER_ID;
import static com.illiapinchuk.moodle.common.TestConstants.UserConstants.LIST_OF_USERS_IDS;
import static com.illiapinchuk.moodle.common.TestConstants.UserConstants.LIST_OF_USERS_WITH_NON_EXISTS_IDS;
import static com.illiapinchuk.moodle.common.TestConstants.UserConstants.NOT_EXISTING_USER_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {MoodleApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import({RedisTestConfiguration.class, MongoInsertData.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class CourseControllerTest {

  @Autowired private TestRestTemplate restTemplate;

  private static final HttpHeaders HEADERS = new HttpHeaders();
  private static final HttpEntity<CourseDto> HTTP_ENTITY = new HttpEntity<>(HEADERS);

  @BeforeAll
  public void getJwtToken() {
    final var response = restTemplate.postForEntity(LOGIN_PATH, EXISTING_ADMIN_AUTH_DTO, Map.class);
    final var token = (String) response.getBody().get("token");
    HEADERS.add("token", token);
    HEADERS.setContentType(MediaType.APPLICATION_JSON);
  }

  @Test
  void getCoursesForStudent_WithAdminUser_ShouldReturnCourses() {
    final var entity = new HttpEntity<>(HEADERS);

    final var resp =
        restTemplate.exchange(
            GET_STUDENT_COURSES_CONTROLLER_PATH,
            HttpMethod.GET,
            entity,
            new ParameterizedTypeReference<List<CourseDto>>() {},
            4);

    assertEquals(HttpStatus.OK, resp.getStatusCode());
    assertNotNull(resp.getBody());
    assertEquals(2, resp.getBody().size());
  }

  @Test
  void getMyCourses_WithAuthenticatedUser_ShouldReturnCourses() {
    final var entity = new HttpEntity<>(HEADERS);

    final var resp =
        restTemplate.exchange(
            GET_AUTH_USER_COURSES_CONTROLLER_PATH,
            HttpMethod.GET,
            entity,
            new ParameterizedTypeReference<List<CourseDto>>() {});

    assertEquals(HttpStatus.OK, resp.getStatusCode());
    assertNotNull(resp.getBody());
    assertEquals(2, resp.getBody().size());
  }

  @Test
  void whenRemovingStudentFromExistingCourseWithAdminUserShouldReturnOkStatus() {
    final var entity = new HttpEntity<>(HEADERS);

    final var resp =
        restTemplate.exchange(
            REMOVE_STUDENT_FROM_COURSE_CONTROLLER_PATH,
            HttpMethod.DELETE,
            entity,
            Void.class,
            VALID_COURSE_WITH_STUDENTS_ID_FOR_REMOVING_STUDENT,
            EXISTING_USER_ID);

    assertEquals(HttpStatus.OK, resp.getStatusCode());
  }

  @Test
  void whenRemovingNotExistingStudentFromExistingCourseWithAdminUserShouldReturnOkStatus() {
    final var entity = new HttpEntity<>(HEADERS);

    final var resp =
        restTemplate.exchange(
            REMOVE_STUDENT_FROM_COURSE_CONTROLLER_PATH,
            HttpMethod.DELETE,
            entity,
            Void.class,
            VALID_COURSE_WITH_STUDENTS_ID_FOR_REMOVING_STUDENT,
            NOT_EXISTING_USER_ID);

    assertEquals(HttpStatus.OK, resp.getStatusCode());
  }

  @Test
  void whenRemovingExistingStudentFromNotExistingCourseWithAdminUserShouldReturnNotFoundStatus() {
    final var entity = new HttpEntity<>(HEADERS);

    final var resp =
        restTemplate.exchange(
            REMOVE_STUDENT_FROM_COURSE_CONTROLLER_PATH,
            HttpMethod.DELETE,
            entity,
            Void.class,
            INVALID_COURSE_ID,
            EXISTING_USER_ID);

    assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
  }

  @Test
  void whenAddingStudentsToExistingCourseWithAdminUserShouldReturnOkStatus() {
    final var entityWithCourseDto = new HttpEntity<>(LIST_OF_USERS_IDS, HEADERS);

    final var resp =
        restTemplate.exchange(
            ADD_STUDENTS_TO_COURSE_CONTROLLER_PATH,
            HttpMethod.POST,
            entityWithCourseDto,
            Void.class,
            VALID_COURSE_WITH_STUDENTS_ID);

    assertEquals(HttpStatus.OK, resp.getStatusCode());
  }

  @Test
  void whenAddingStudentsToNonExistingCourseShouldReturnNotFoundStatus() {
    final var entityWithStudents = new HttpEntity<>(LIST_OF_USERS_IDS, HEADERS);

    final var resp =
        restTemplate.exchange(
            ADD_STUDENTS_TO_COURSE_CONTROLLER_PATH,
            HttpMethod.POST,
            entityWithStudents,
            Void.class,
            NOT_VALID_COURSE_ID);

    assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
  }

  @Test
  void whenAddingStudentsWithNonExistingStudentShouldReturnNotFoundStatus() {
    final var entityWithStudents = new HttpEntity<>(LIST_OF_USERS_WITH_NON_EXISTS_IDS, HEADERS);

    final var resp =
        restTemplate.exchange(
            ADD_STUDENTS_TO_COURSE_CONTROLLER_PATH,
            HttpMethod.POST,
            entityWithStudents,
            Void.class,
            VALID_COURSE_WITH_STUDENTS_ID);

    assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
  }

  @Test
  void whenExistingCourseIdAndAdminUserShouldReturnOKStatus() {
    final var resp =
        restTemplate.exchange(
            COURSE_WITH_ID_CONTROLLER_PATH,
            HttpMethod.GET,
            HTTP_ENTITY,
            CourseDto.class,
            VALID_COURSE_ID);

    assertEquals(HttpStatus.OK, resp.getStatusCode());
  }

  @Test
  void whenExistingCourseIdAndNotAdminUserAndNotAllowedToSeeCourseShouldReturnOKStatus() {
    final HttpHeaders headers = new HttpHeaders();
    final HttpEntity<CourseDto> httpEntity = new HttpEntity<>(headers);

    final var response =
        restTemplate.postForEntity(LOGIN_PATH, EXISTING_USER_LOGIN_AUTH_DTO, Map.class);
    final var token = (String) response.getBody().get("token");
    headers.add("token", token);
    headers.setContentType(MediaType.APPLICATION_JSON);

    final var resp =
        restTemplate.exchange(
            COURSE_WITH_ID_CONTROLLER_PATH,
            HttpMethod.GET,
            httpEntity,
            String.class,
            VALID_COURSE_ID);

    assertEquals(HttpStatus.FORBIDDEN, resp.getStatusCode());
  }

  @Test
  void whenNotExistingCourseIdShouldReturnNotFoundStatus() {
    final var resp =
        restTemplate.exchange(
            COURSE_WITH_ID_CONTROLLER_PATH,
            HttpMethod.GET,
            HTTP_ENTITY,
            String.class,
            INVALID_COURSE_ID);

    assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
  }

  @Test
  void whenCourseDtoIsValidShouldReturnCreateStatus() {
    final var entityWithCourseDto = new HttpEntity<>(VALID_COURSE_DTO, HEADERS);

    final var resp =
        restTemplate.exchange(
            COURSE_CONTROLLER_PATH, HttpMethod.POST, entityWithCourseDto, String.class);

    assertEquals(HttpStatus.CREATED, resp.getStatusCode());
  }

  @Test
  void whenCourseDtoIsValidAndHasManyAuthorsShouldReturnCreateStatus() {
    final var entityWithCourseDto = new HttpEntity<>(VALID_COURSE_DTO_WITH_TWO_AUTHORS, HEADERS);

    final var resp =
        restTemplate.exchange(
            COURSE_CONTROLLER_PATH, HttpMethod.POST, entityWithCourseDto, String.class);

    assertEquals(HttpStatus.CREATED, resp.getStatusCode());
  }

  @Test
  void whenCourseDtoWithoutAuthorsShouldReturnBadRequestStatus() {
    final var entityWithCourseDto = new HttpEntity<>(COURSE_DTO_WITHOUT_AUTHORS, HEADERS);

    final var resp =
        restTemplate.exchange(
            COURSE_CONTROLLER_PATH, HttpMethod.POST, entityWithCourseDto, String.class);

    assertEquals(HttpStatus.BAD_REQUEST, resp.getStatusCode());
  }

  @Test
  void whenUpdatingExistingCourseShouldReturnOkStatus() {
    final var entityWithCourseDto = new HttpEntity<>(VALID_COURSE_DTO_AS_STRING, HEADERS);

    final var resp =
        restTemplate.exchange(
            COURSE_CONTROLLER_PATH, HttpMethod.PUT, entityWithCourseDto, CourseDto.class);

    assertEquals(HttpStatus.OK, resp.getStatusCode());
  }

  @Test
  void whenUpdatingNotExistingCourseShouldReturnNotFoundStatus() {
    final var entityWithCourseDto = new HttpEntity<>(COURSE_DTO_INVALID_ID_AS_STRING, HEADERS);

    final var resp =
        restTemplate.exchange(
            COURSE_CONTROLLER_PATH, HttpMethod.PUT, entityWithCourseDto, String.class);

    assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
  }

  @Test
  void whenDeletingExistingCourseShouldReturnOkStatus() {
    final var resp =
        restTemplate.exchange(
            COURSE_WITH_ID_CONTROLLER_PATH,
            HttpMethod.DELETE,
            HTTP_ENTITY,
            String.class,
            VALID_COURSE_ID_TO_DELETE);

    assertEquals(HttpStatus.OK, resp.getStatusCode());
  }

  @Test
  void whenDeletingNotExistingCourseShouldReturnNotFoundStatus() {
    final var resp =
        restTemplate.exchange(
            COURSE_WITH_ID_CONTROLLER_PATH,
            HttpMethod.DELETE,
            HTTP_ENTITY,
            String.class,
            INVALID_COURSE_ID);

    assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
  }
}
