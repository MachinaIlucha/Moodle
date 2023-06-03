package com.illiapinchuk.moodle.persistence.repository;

import com.illiapinchuk.moodle.persistence.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Basic repository for user.
 */
public interface UserRepository extends JpaRepository<User, Long> {
}
