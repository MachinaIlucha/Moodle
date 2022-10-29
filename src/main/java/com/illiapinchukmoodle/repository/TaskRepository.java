package com.illiapinchukmoodle.repository;

import com.illiapinchukmoodle.data.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
