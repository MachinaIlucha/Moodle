package com.illiapinchuk.moodle.config;

import com.illiapinchuk.moodle.model.entity.TaskStatus;
import com.illiapinchuk.moodle.persistence.entity.Course;
import com.illiapinchuk.moodle.persistence.entity.Task;
import com.illiapinchuk.moodle.persistence.repository.CourseRepository;
import com.illiapinchuk.moodle.persistence.repository.TaskRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;

@TestConfiguration
public class MongoInsertData {

  @Bean
  public CommandLineRunner initData(
      CourseRepository courseRepository, TaskRepository taskRepository) {
    return args -> {
      courseRepository.deleteAll();
      taskRepository.deleteAll();

      final Course VALID_COURSE_1 =
          Course.builder()
              .id("1")
              .name("Test Java Course")
              .description("Test This is java core course.")
              .build();
      final Course VALID_COURSE_2 =
          Course.builder()
              .id("2")
              .name("Test Java Course 2")
              .description("Test This is java core course 2.")
              .build();
      final Course VALID_COURSE_3 =
              Course.builder()
                      .id("3")
                      .name("Test Java Course 3")
                      .description("Test This is java core course 3.")
                      .students(new HashSet<>(List.of(1L, 2L, 3L)))
                      .build();

      // save all courses
      courseRepository.saveAll(List.of(VALID_COURSE_1, VALID_COURSE_2, VALID_COURSE_3));

      final Task VALID_TASK_1 =
          Task.builder()
              .id("1")
              .title("Task 1")
              .description("Complete assignment")
              .dueDate(new Date())
              .creationDate(new Date())
              .course(VALID_COURSE_1)
              .authorId(2L)
              .status(TaskStatus.OPEN)
              .attachments(new ArrayList<>())
              .submissions(new ArrayList<>())
              .build();
      final Task VALID_TASK_2 =
          Task.builder()
              .id("2")
              .title("Task 2")
              .description("Review chapter 5")
              .dueDate(new Date())
              .creationDate(new Date())
              .course(VALID_COURSE_2)
              .authorId(3L)
              .status(TaskStatus.OPEN)
              .attachments(new ArrayList<>())
              .submissions(new ArrayList<>())
              .build();
      final Task VALID_TASK_3 =
          Task.builder()
              .id("3")
              .title("Task 3")
              .description("Prepare presentation")
              .dueDate(new Date())
              .creationDate(new Date())
              .course(VALID_COURSE_2)
              .authorId(1L)
              .status(TaskStatus.OPEN)
              .attachments(new ArrayList<>())
              .submissions(new ArrayList<>())
              .build();

      // save all tasks
      taskRepository.saveAll(List.of(VALID_TASK_1, VALID_TASK_2, VALID_TASK_3));
    };
  }
}
