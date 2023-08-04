package com.illiapinchuk.moodle;

import com.icegreen.greenmail.configuration.GreenMailConfiguration;
import com.icegreen.greenmail.junit5.GreenMailExtension;
import com.icegreen.greenmail.util.ServerSetupTest;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.util.regex.Pattern;

import static com.illiapinchuk.moodle.common.TestConstants.Path.PASSWORD_RESET_INIT_PATH;
import static com.illiapinchuk.moodle.common.TestConstants.Path.PASSWORD_RESET_PATH;
import static com.illiapinchuk.moodle.common.TestConstants.User.EXISTING_ADMIN_EMAIL;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
    classes = {MoodleApplication.class})
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class PasswordRecoveryTests {

  @RegisterExtension
  static GreenMailExtension greenMail =
      new GreenMailExtension(ServerSetupTest.SMTP)
          .withConfiguration(GreenMailConfiguration.aConfig().withUser("user", "admin"))
          .withPerMethodLifecycle(false);

  @Autowired private TestRestTemplate restTemplate;

  private String RESET_TOKEN;

  @BeforeAll
  public void getResetToken() throws MessagingException, IOException {
    final var builder =
        UriComponentsBuilder.fromUriString(PASSWORD_RESET_INIT_PATH)
            .queryParam("email", EXISTING_ADMIN_EMAIL);

    restTemplate.postForEntity(builder.toUriString(), null, Void.class);
    greenMail.waitForIncomingEmail(2000, 1);

    final var messages = greenMail.getReceivedMessages();
    final var content = messages[0].getContent().toString();

    final var pattern = Pattern.compile("token=([a-f0-9\\-]+)");
    final var matcher = pattern.matcher(content);

    if (matcher.find()) {
      RESET_TOKEN = matcher.group(1); // Group 1 will contain the token value
    } else {
      throw new IllegalStateException("Could not find token in email content");
    }
  }

  @Test
  void testPasswordResetWhenTokenIsValidShouldReturnOkStatus() {
    final var newPassword = "new-password";
    final var urlBuilder =
        UriComponentsBuilder.fromUriString(PASSWORD_RESET_PATH)
            .queryParam("token", RESET_TOKEN)
            .queryParam("password", newPassword);

    final var response =
        restTemplate.exchange(urlBuilder.toUriString(), HttpMethod.PUT, null, String.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
  }

  @Test
  void testPasswordResetWhenTokenWasChangedShouldReturnNotFoundStatus() {
    final var newPassword = "new-password";
    final var urlBuilder =
        UriComponentsBuilder.fromUriString(PASSWORD_RESET_PATH)
            .queryParam("token", RESET_TOKEN + "some-changes")
            .queryParam("password", newPassword);

    final var response =
        restTemplate.exchange(urlBuilder.toUriString(), HttpMethod.PUT, null, String.class);

    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
  }
}
