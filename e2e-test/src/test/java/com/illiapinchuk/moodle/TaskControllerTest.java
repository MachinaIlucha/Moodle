package com.illiapinchuk.moodle;

import com.illiapinchuk.moodle.config.MongoInsertData;
import com.illiapinchuk.moodle.config.RedisTestConfiguration;
import com.illiapinchuk.moodle.model.dto.TaskDto;
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
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.LinkedMultiValueMap;

import java.io.IOException;
import java.util.Map;

import static com.illiapinchuk.moodle.common.TestConstants.Dto.Auth.EXISTING_ADMIN_AUTH_DTO;
import static com.illiapinchuk.moodle.common.TestConstants.Path.LOGIN_PATH;
import static com.illiapinchuk.moodle.common.TestConstants.Path.TASK_CONTROLLER_PATH;
import static com.illiapinchuk.moodle.common.TestConstants.Path.TASK_UPLOAD_ATTACHMENT_CONTROLLER_PATH;
import static com.illiapinchuk.moodle.common.TestConstants.Path.TASK_WITH_ID_CONTROLLER_PATH;
import static com.illiapinchuk.moodle.common.TestConstants.TaskConstants.INVALID_TASK_DTO_NOT_EXISTED_AUTHOR_ID;
import static com.illiapinchuk.moodle.common.TestConstants.TaskConstants.INVALID_TASK_DTO_NOT_EXISTED_COURSE_ID;
import static com.illiapinchuk.moodle.common.TestConstants.TaskConstants.NOT_EXISTING_TASK_ID;
import static com.illiapinchuk.moodle.common.TestConstants.TaskConstants.VALID_TASK_DTO;
import static com.illiapinchuk.moodle.common.TestConstants.TaskConstants.VALID_TASK_ID;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {MoodleApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import({RedisTestConfiguration.class, MongoInsertData.class})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class TaskControllerTest {

  @Autowired private TestRestTemplate restTemplate;

  private static final HttpHeaders HEADERS = new HttpHeaders();
  private static final HttpEntity<Object> HTTP_ENTITY = new HttpEntity<>(HEADERS);

  @BeforeAll
  public void getJwtToken() {
    final var response = restTemplate.postForEntity(LOGIN_PATH, EXISTING_ADMIN_AUTH_DTO, Map.class);
    final var token = (String) response.getBody().get("token");
    HEADERS.add("token", token);
    HEADERS.setContentType(MediaType.APPLICATION_JSON);
  }

  @Test
  void whenExistingTaskIdShouldReturnOKStatus() {
    final var resp =
        restTemplate.exchange(
            TASK_WITH_ID_CONTROLLER_PATH,
            HttpMethod.GET,
            HTTP_ENTITY,
            TaskDto.class,
            VALID_TASK_ID);

    assertEquals(HttpStatus.OK, resp.getStatusCode());
  }

  @Test
  void whenNotExistingTaskIdShouldReturnNotFoundStatus() {
    final var resp =
        restTemplate.exchange(
            TASK_WITH_ID_CONTROLLER_PATH,
            HttpMethod.GET,
            HTTP_ENTITY,
            String.class,
            NOT_EXISTING_TASK_ID);

    assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
  }

  @Test
  void whenTaskDtoIsValidShouldReturnCreateStatus() {
    final var entityWithTaskDto = new HttpEntity<>(VALID_TASK_DTO, HEADERS);

    final var resp =
        restTemplate.exchange(
            TASK_CONTROLLER_PATH, HttpMethod.POST, entityWithTaskDto, TaskDto.class);

    assertEquals(HttpStatus.CREATED, resp.getStatusCode());
  }

  @Test
  void whenTaskDtoHasNotExistedCourseIdShouldReturnNotFoundStatus() {
    final var entityWithTaskDto = new HttpEntity<>(INVALID_TASK_DTO_NOT_EXISTED_COURSE_ID, HEADERS);

    final var resp =
        restTemplate.exchange(
            TASK_CONTROLLER_PATH, HttpMethod.POST, entityWithTaskDto, String.class);

    assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
  }

  @Test
  void whenTaskDtoHasNotExistedAuthorIdShouldReturnNotFoundStatus() {
    final var entityWithTaskDto = new HttpEntity<>(INVALID_TASK_DTO_NOT_EXISTED_AUTHOR_ID, HEADERS);

    final var resp =
        restTemplate.exchange(
            TASK_CONTROLLER_PATH, HttpMethod.POST, entityWithTaskDto, String.class);

    assertEquals(HttpStatus.NOT_FOUND, resp.getStatusCode());
  }
}
