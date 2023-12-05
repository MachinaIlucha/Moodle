package com.illiapinchuk.moodle.service.business;

import com.illiapinchuk.moodle.persistence.entity.TaskAttachment;
import java.util.List;

/** This interface represents a service for managing task attachments. */
public interface TaskAttachmentService {

  /**
   * Retrieves a list of attachments associated with a given task id.
   *
   * @param taskId The task id for which attachments are to be retrieved.
   * @return A list of TaskAttachment objects representing the attachments.
   */
  List<TaskAttachment> getAttachmentsByTaskId(String taskId);

  /**
   * Saves a task attachment.
   *
   * @param taskAttachment The TaskAttachment object to be saved.
   * @return The saved TaskAttachment object.
   */
  TaskAttachment saveTaskAttachment(TaskAttachment taskAttachment);

  /**
   * Deletes a task attachment.
   *
   * @param taskAttachment The TaskAttachment object to be deleted.
   */
  void deleteTaskAttachment(TaskAttachment taskAttachment);
}
