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
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Map;

import static com.illiapinchuk.moodle.common.TestConstants.Course.*;
import static com.illiapinchuk.moodle.common.TestConstants.Dto.Auth.EXISTING_ADMIN_AUTH_DTO;
import static com.illiapinchuk.moodle.common.TestConstants.Path.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
  void whenExistingCourseIdShouldReturnOKStatus() {
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
