package com.illiapinchukmoodle.service.interfacies;

import com.illiapinchukmoodle.data.model.Task;

import java.util.List;
import java.util.Optional;

/**
 * Service for {@link com.illiapinchukmoodle.data.model.Task}
 * @author Illia Pinchuk
 */
public interface TaskService {
    List<Task> getAllTasks();
    Task createTask(Task task);
    Optional<Task> getTaskById(Long taskId);
    Task updateTask(Task task, Long taskId);
    Task deleteTask(Long taskId);
}
