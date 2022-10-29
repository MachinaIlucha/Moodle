package com.illiapinchukmoodle.service;

import com.illiapinchukmoodle.data.model.Course;
import com.illiapinchukmoodle.data.model.Task;
import com.illiapinchukmoodle.data.model.TaskProgress;
import com.illiapinchukmoodle.data.model.User;
import com.illiapinchukmoodle.exception.TaskNotFoundException;
import com.illiapinchukmoodle.exception.TaskProgressNotFoundException;
import com.illiapinchukmoodle.exception.UserNotFoundException;
import com.illiapinchukmoodle.repository.TaskProgressRepository;
import com.illiapinchukmoodle.service.interfacies.TaskProgressService;
import com.illiapinchukmoodle.service.interfacies.TaskService;
import com.illiapinchukmoodle.service.interfacies.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

@Service
public class TaskProgressImpl implements TaskProgressService {

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    @Autowired
    private TaskProgressRepository taskProgressRepository;

    @Override
    public List<TaskProgress> getAllTaskProgressForUserIdAndCourseId(Long userId, Long courseId) {
        return taskProgressRepository.getTaskProgressesByUserIdAndCourseId(userId, courseId);
    }

    @Override
    public List<TaskProgress> getAllTaskProgressForUserId(Long userId) {
        return taskProgressRepository.getTaskProgressesByUserId(userId);
    }

    @Override
    public TaskProgress saveTaskProgress(TaskProgress taskProgress) {
        return taskProgressRepository.save(taskProgress);
    }

    @Override
    public TaskProgress updateTaskProgress(TaskProgress taskProgressRequest, Long taskProgressId) {
        TaskProgress taskProgress = taskProgressRepository.findById(taskProgressId)
                .orElseThrow(() -> new TaskProgressNotFoundException(taskProgressId));

        Long taskId = taskProgressRequest.getTask().getId();
        Task task = taskService.getTaskById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        taskProgress.setTask(task);

        Long userId = taskProgressRequest.getUser().getId();
        User user = userService.getUserById(userId)
                        .orElseThrow(() -> new UserNotFoundException(userId));
        taskProgress.setUser(user);
        taskProgress.setAttachments(taskProgressRequest.getAttachments());
        taskProgress.setScore(taskProgressRequest.getScore());
        taskProgress.setVerified(taskProgress.isVerified());
        return taskProgressRepository.save(taskProgress);
    }

    @Override
    public void deleteTaskProgressesByUserIdAndTaskIn(Long user_id, Collection<Task> task) {
        taskProgressRepository.deleteTaskProgressesByUserIdAndTaskIn(user_id, task);
    }

    @Override
    public void deleteTaskProgressesByTaskId(Long taskId) {
        taskProgressRepository.deleteTaskProgressesByTaskId(taskId);
    }
}
