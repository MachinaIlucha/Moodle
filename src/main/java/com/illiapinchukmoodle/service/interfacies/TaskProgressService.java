package com.illiapinchukmoodle.service.interfacies;

import com.illiapinchukmoodle.data.model.Task;
import com.illiapinchukmoodle.data.model.TaskProgress;

import java.util.Collection;
import java.util.List;

/**
 * Service for {@link com.illiapinchukmoodle.data.model.TaskProgress}
 * @author Illia Pinchuk
 */
public interface TaskProgressService {
    TaskProgress saveTaskProgress(TaskProgress taskProgress);
    TaskProgress updateTaskProgress(TaskProgress taskProgress, Long taskProgressId);
    List<TaskProgress> getAllTaskProgressForUserId(Long userId);
    List<TaskProgress> getAllTaskProgressForUserIdAndCourseId(Long userId, Long courseId);
    void deleteTaskProgressesByUserIdAndTaskIn(Long user_id, Collection<Task> task);
    void deleteTaskProgressesByTaskId(Long taskId);
}
