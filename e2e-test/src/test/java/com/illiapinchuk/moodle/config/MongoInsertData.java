package com.illiapinchuk.moodle.config;

import com.illiapinchuk.moodle.persistence.entity.Course;
import com.illiapinchuk.moodle.persistence.repository.CourseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.List;

@TestConfiguration
public class MongoInsertData {

  @Bean
  public CommandLineRunner initData(CourseRepository repository) {
    return args -> {
      repository.deleteAll();

      final var validCourse =
          Course.builder()
              .id("1")
              .name("Test Java Course")
              .description("Test This is java core course.")
              .build();

      final var validCourse2 =
          Course.builder()
              .id("2")
              .name("Test Java Course to delete")
              .description("Test This is java core course.")
              .build();

      // save all courses
      repository.saveAll(List.of(validCourse, validCourse2));
    };
  }
}
