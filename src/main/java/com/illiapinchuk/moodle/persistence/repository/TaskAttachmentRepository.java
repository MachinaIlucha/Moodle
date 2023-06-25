package com.illiapinchuk.moodle.persistence.repository;

import com.illiapinchuk.moodle.persistence.entity.TaskAttachment;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

/** Basic repository for taskAttachment. */
public interface TaskAttachmentRepository extends MongoRepository<TaskAttachment, String> {

  List<TaskAttachment> getTaskAttachmentsByTaskId(String taskId);
}
