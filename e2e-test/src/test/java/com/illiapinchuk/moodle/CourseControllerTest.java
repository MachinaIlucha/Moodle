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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Map;

import static com.illiapinchuk.moodle.common.TestConstants.Course.INVALID_COURSE_ID;
import static com.illiapinchuk.moodle.common.TestConstants.Course.VALID_COURSE_ID;
import static com.illiapinchuk.moodle.common.TestConstants.Dto.Auth.EXISTING_ADMIN_AUTH_DTO;
import static com.illiapinchuk.moodle.common.TestConstants.Path.COURSE_GET_BY_ID_CONTROLLER_PATH;
import static com.illiapinchuk.moodle.common.TestConstants.Path.LOGIN_PATH;
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

  @BeforeAll
  public void getJwtToken() {
    final var response = restTemplate.postForEntity(LOGIN_PATH, EXISTING_ADMIN_AUTH_DTO, Map.class);
    final var token = (String) response.getBody().get("token");
    HEADERS.add("token", token);
  }

  @Test
  void whenExistingCourseIdShouldReturnOKStatus() {
    final var entity = new HttpEntity<>(HEADERS);
    final var resp =
        restTemplate.exchange(
            COURSE_GET_BY_ID_CONTROLLER_PATH,
            HttpMethod.GET,
            entity,
            CourseDto.class,
            VALID_COURSE_ID);

    assertEquals(HttpStatus.OK, resp.getStatusCode());
  }

  @Test
  void whenNotExistingCourseIdShouldReturnNotFoundStatus() {
    final var entity = new HttpEntity<>(HEADERS);
    final var resp =
            restTemplate.exchange(
                    COURSE_GET_BY_ID_CONTROLLER_PATH,
                    HttpMethod.GET,
                    entity,
                    String.class,
                    INVALID_COURSE_ID);

    assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
  }
}
