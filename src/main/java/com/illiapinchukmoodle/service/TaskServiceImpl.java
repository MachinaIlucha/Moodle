package com.illiapinchukmoodle.service;

import com.illiapinchukmoodle.data.model.Course;
import com.illiapinchukmoodle.data.model.Task;
import com.illiapinchukmoodle.data.model.TaskProgress;
import com.illiapinchukmoodle.exception.CourseNotFoundException;
import com.illiapinchukmoodle.exception.TaskNotFoundException;
import com.illiapinchukmoodle.repository.TaskRepository;
import com.illiapinchukmoodle.service.interfacies.CourseService;
import com.illiapinchukmoodle.service.interfacies.TaskProgressService;
import com.illiapinchukmoodle.service.interfacies.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CourseService courseService;
    @Autowired
    private TaskProgressService taskProgressService;

    @Override
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    @Override
    public Task createTask(Task task) {
        Long courseId = task.getCourse().getId();
        /* We are using getCourseWithUsers 'couse we need to init courseUsers */
        Course course = courseService.getCourseWithUsers(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        /* When we are creating new task we also need to create taskProgress for everyone in this course for this task */
        course.getCourseUsers().forEach(user -> {
            TaskProgress taskProgress = new TaskProgress();
            taskProgress.setUser(user);
            taskProgress.setVerified(false);
            taskProgress.setTask(task);

            taskProgressService.saveTaskProgress(taskProgress);
        });

        return taskRepository.save(task);
    }

    @Override
    public Task updateTask(Task taskRequest, Long taskId) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));

        task.setName(taskRequest.getName());
        task.setDescription(taskRequest.getDescription());
        Long courseId = taskRequest.getCourse().getId();
        Course course = courseService.getCourseById(courseId)
                .orElseThrow(() -> new TaskNotFoundException(courseId));
        task.setCourse(course);
        return taskRepository.save(task);
    }

    @Override
    public Optional<Task> getTaskById(Long taskId) {
        return taskRepository.findById(taskId);
    }

    @Override
    public void deleteTask(Long taskId) {
        Task task = getTaskById(taskId)
                .orElseThrow(() -> new TaskNotFoundException(taskId));
        /* We also need to delete all task progresses of this task for all users*/
        taskProgressService.deleteTaskProgressesByTaskId(taskId);
        taskRepository.delete(task);
    }
}
