package com.illiapinchuk.moodle.service.crud;

import com.illiapinchuk.moodle.persistence.entity.Grade;

/** Provides CRUD (Create, Read, Update, Delete) service operations for {@link Grade} entities. */
public interface GradeCRUDService {

  /**
   * Saves a {@link Grade} object to the database. If the grade is new, it will be created;
   * otherwise, the existing grade will be updated.
   *
   * @param grade The {@link Grade} object to be saved.
   * @return The saved {@link Grade} object, which may include additional updates (like generated
   *     id) performed by the persistence layer.
   */
  Grade saveGrade(Grade grade);
}
