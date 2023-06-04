package com.illiapinchuk.moodle.persistence.repository;

import com.illiapinchuk.moodle.persistence.entity.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Basic repository for user.
 */
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findUserByLoginOrEmail(String login, String email);

  boolean existsUserByLogin(String login);
  boolean existsUserByEmail(String email);

  void deleteUserByLoginOrEmail(String login, String email);
}
