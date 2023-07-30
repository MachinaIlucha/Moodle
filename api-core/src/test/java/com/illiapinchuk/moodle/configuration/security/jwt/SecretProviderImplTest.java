package com.illiapinchuk.moodle.configuration.security.jwt;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SecretProviderImplTest {

  private SecretProviderImpl secretProvider;

  @BeforeEach
  void setUp() {
    secretProvider = new SecretProviderImpl("");
    ReflectionTestUtils.setField(secretProvider, "encodedSecret", "VGVzdFNlY3JldFNlY3JldA==");
  }

  @Test
  void testEncodedSecret() {
    final var expectedEncodedSecret = "VGVzdFNlY3JldFNlY3JldA==";

    final var encodedSecret = secretProvider.getEncodedSecret();

    assertEquals(expectedEncodedSecret, encodedSecret);
  }
}
