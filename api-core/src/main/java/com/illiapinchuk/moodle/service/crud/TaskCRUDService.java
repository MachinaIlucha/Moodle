package com.illiapinchuk.moodle.service.crud;

import com.illiapinchuk.moodle.model.dto.TaskDto;
import com.illiapinchuk.moodle.persistence.entity.Task;

/** Provides CRUD (Create, Read, Update, Delete) service operations for {@link Task} entities. */
public interface TaskCRUDService {
    /**
     * Retrieves a task by its ID.
     *
     * @param id The ID of the task to retrieve.
     * @return The task with the specified ID, or null if not found.
     */
    Task getTaskById(String id);

    /**
     * Creates a new task.
     *
     * @param task The task object to create.
     * @return The created task.
     */
    Task createTask(Task task);

    /**
     * Updates an existing task.
     *
     * @param taskDto The task DTO containing the updated task data.
     * @return The updated task.
     */
    Task updateTaskFromDto(TaskDto taskDto);

    /**
     * Updates an existing task.
     *
     * @param task The task containing the updated task data.
     * @return The updated task.
     */
    Task updateTask(Task task);

    /**
     * Deletes a task by its ID.
     *
     * @param id The ID of the task to delete.
     */
    void deleteTaskById(String id);
}
