package com.illiapinchukmoodle.repository;

import com.illiapinchukmoodle.data.model.Task;
import com.illiapinchukmoodle.data.model.TaskProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface TaskProgressRepository extends JpaRepository<TaskProgress, Long> {
    List<TaskProgress> getTaskProgressesByUserId(Long userId);

    @Query("select tp from TaskProgress tp where tp.user.id = :userId and tp.task.course.id = :courseId")
    List<TaskProgress> getTaskProgressesByUserIdAndCourseId(Long userId, Long courseId);

    Optional<TaskProgress> getTaskProgressByUserIdAndTaskId(Long userId, Long taskId);

    @Transactional
    @Modifying
    @Query("delete from TaskProgress tp where tp.user.id = :user_id and (tp.task in :task)")
    void deleteTaskProgressesByUserIdAndTaskIn(Long user_id, Collection<Task> task);

    @Transactional
    @Modifying
    @Query("delete from TaskProgress tp where tp.task.id = :taskId")
    void deleteTaskProgressesByTaskId(Long taskId);
}
