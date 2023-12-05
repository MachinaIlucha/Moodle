package com.illiapinchuk.moodle.service.crud;

import com.illiapinchuk.moodle.persistence.entity.Submission;

/**
 * Provides CRUD (Create, Read, Update, Delete) service operations for {@link Submission} entities.
 */
public interface SubmissionCRUDService {

  /**
   * Saves a new Submission object in the system.
   *
   * @param submission The Submission object to be saved.
   * @return The saved Submission object.
   */
  Submission saveSubmission(Submission submission);

  /**
   * Retrieves a Submission object by its unique identifier.
   *
   * @param id The unique identifier of the Submission to retrieve.
   * @return The retrieved Submission object, or null if not found.
   */
  Submission getSubmissionById(String id);
}
