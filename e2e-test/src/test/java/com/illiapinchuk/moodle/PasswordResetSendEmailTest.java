package com.illiapinchuk.moodle;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import com.illiapinchuk.moodle.config.RedisTestConfiguration;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.UriComponentsBuilder;

import static com.illiapinchuk.moodle.common.TestConstants.Path.PASSWORD_RESET_INIT_PATH;
import static com.illiapinchuk.moodle.common.TestConstants.UserConstants.EXISTING_ADMIN_EMAIL;
import static com.illiapinchuk.moodle.common.TestConstants.UserConstants.NOT_EXISTING_ADMIN_EMAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {MoodleApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@Import(RedisTestConfiguration.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class PasswordResetSendEmailTest {

  @RegisterExtension
  static GreenMailExtension greenMail =
      new GreenMailExtension(ServerSetupTest.SMTP)
          .withConfiguration(GreenMailConfiguration.aConfig().withUser("user", "admin"))
          .withPerMethodLifecycle(false);

  @Autowired private TestRestTemplate restTemplate;

  @AfterEach
  void clearGreenMail() {
    greenMail.reset();
  }

  @Test
  void testForgotPasswordWithValidEmailShouldReturnNoContentStatusAndReceiveEmail() {
    final var urlBuilder =
        UriComponentsBuilder.fromUriString(PASSWORD_RESET_INIT_PATH)
            .queryParam("email", EXISTING_ADMIN_EMAIL);

    final var response = restTemplate.postForEntity(urlBuilder.toUriString(), null, Void.class);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    assertTrue(greenMail.waitForIncomingEmail(10000, 1));
  }

  @Test
  void testForgotPasswordWithInvalidEmailShouldReturnNoContentStatusAndDoNotReceiveEmail() {
    final var urlBuilder =
        UriComponentsBuilder.fromUriString(PASSWORD_RESET_INIT_PATH)
            .queryParam("email", NOT_EXISTING_ADMIN_EMAIL);

    final var response = restTemplate.postForEntity(urlBuilder.toUriString(), null, Void.class);

    assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    assertTrue(greenMail.waitForIncomingEmail(2000, 0));
  }
}
