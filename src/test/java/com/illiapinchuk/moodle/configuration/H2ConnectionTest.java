package com.illiapinchuk.moodle.configuration;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class H2ConnectionTest {

  @PersistenceContext
  private EntityManager entityManager;

  @Test
  void testDatabaseConnection() {
    // Check connection to db
    assertThat(entityManager).isNotNull();
  }
}
