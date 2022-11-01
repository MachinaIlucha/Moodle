package com.illiapinchukmoodle.repository;

import com.illiapinchukmoodle.data.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Illia Pinchuk
 */
public interface TaskRepository extends JpaRepository<Task, Long> {
}
