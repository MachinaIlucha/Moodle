package com.illiapinchuk.moodle.persistence.repository;

import com.illiapinchuk.moodle.persistence.entity.Task;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

/** Basic repository for task. */
public interface TaskRepository extends MongoRepository<Task, String> {

  List<Task> getTasksByCourseId(String courseId);
}
