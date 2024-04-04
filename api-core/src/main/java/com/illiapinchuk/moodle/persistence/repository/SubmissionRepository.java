package com.illiapinchuk.moodle.persistence.repository;

import com.illiapinchuk.moodle.persistence.entity.Submission;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface SubmissionRepository extends MongoRepository<Submission, String> {

  List<Submission> getSubmissionsByTaskIdAndAndUserId(String taskId, Long userId);

  List<Submission> getSubmissionsByTaskId(String taskId);
}
