package com.illiapinchuk.moodle.persistence.repository;

import com.illiapinchuk.moodle.persistence.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/** Repository interface for managing UserToken entities in the database. */
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
  void deleteUserTokenByUserId(Long userId);

  Optional<UserToken> getUserTokenByToken(String token);
}
