package com.illiapinchukmoodle.service;

import com.illiapinchukmoodle.data.model.Course;
import com.illiapinchukmoodle.data.model.Status;
import com.illiapinchukmoodle.data.model.TaskProgress;
import com.illiapinchukmoodle.data.model.User;
import com.illiapinchukmoodle.exception.CourseNotFoundException;
import com.illiapinchukmoodle.exception.UserNotFoundException;
import com.illiapinchukmoodle.repository.CourseRepository;
import com.illiapinchukmoodle.service.interfacies.CourseService;
import com.illiapinchukmoodle.service.interfacies.TaskProgressService;
import com.illiapinchukmoodle.service.interfacies.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Implementation for {@link com.illiapinchukmoodle.service.interfacies.CourseService}
 * @author Illia Pinchuk
 */
@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private UserService userService;

    @Autowired
    private TaskProgressService taskProgressService;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }

    @Override
    public Course createCourse(Course course) {
        return courseRepository.save(course);
    }

    @Override
    public Optional<Course> getCourseById(Long courseId) {
        return courseRepository.findById(courseId);
    }

    @Override
    public Course updateCourse(Course courseRequest, Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        course.setName(courseRequest.getName());
        course.setDescription(courseRequest.getDescription());
        return courseRepository.save(course);
    }

    @Override
    public Course deleteCourse(Long courseId) {
        Course course = getCourseById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
        course.setStatus(Status.DELETED);
        course.setUpdated(new Date());
        return courseRepository.save(course);
    }

    @Override
    public void deleteCourseFromUser(Long courseId, Long userId) {
        Course course = getCourseWithTasks(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        course.getCourseUsers().remove(user);

        taskProgressService.deleteTaskProgressesByUserIdAndTaskIn(userId, course.getTasks());
        courseRepository.save(course);
    }

    @Override
    @Transactional
    public Course addCourseToUser(Long courseId, Long userId) {
        /* We are using getCourseWithTasks 'couse we need to init tasks on course */
        Course course = getCourseWithTasks(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
        User user = userService.getUserById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        user.getUserCourses().add(course);

        /* When we are adding new course to user we need aether create taskProgress for every task in this course for this user */
        course.getTasks().forEach(task ->{
            TaskProgress taskProgress = new TaskProgress();
            taskProgress.setUser(user);
            taskProgress.setTask(task);
            taskProgress.setVerified(false);

            taskProgressService.saveTaskProgress(taskProgress);
        });

        return courseRepository.save(course);
    }

    @Override
    public Integer getCourseProgressByUser(Long courseId, Long userId) {
        List<TaskProgress> taskProgressesOfCourseForUser = taskProgressService.getAllTaskProgressForUserIdAndCourseId(userId, courseId);
        int countOfTaskProgressesOfCourseForUser = taskProgressesOfCourseForUser.size();
        if (countOfTaskProgressesOfCourseForUser == 0)
            return 0;
        long finishedTasks = taskProgressesOfCourseForUser.stream().filter(TaskProgress::isVerified).count();

        return (int) (finishedTasks * 100/countOfTaskProgressesOfCourseForUser);
    }

    @Override
    public Optional<Course> getCourseWithUsers(Long courseId) {
        return courseRepository.getCourseWithUsers(courseId);
    }

    @Override
    public Optional<Course> getCourseWithTasks(Long courseId) {
        return Optional.of(courseRepository.getCourseWithTasks(courseId));
    }
}
