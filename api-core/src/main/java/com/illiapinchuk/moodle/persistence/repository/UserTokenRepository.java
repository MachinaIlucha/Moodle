package com.illiapinchuk.moodle.persistence.repository;

import com.illiapinchuk.moodle.model.entity.UserTokenStatus;
import com.illiapinchuk.moodle.persistence.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

/** Repository interface for managing UserToken entities in the database. */
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
  void deleteUserTokenByUserId(Long userId);

  Optional<UserToken> getUserTokenByToken(String token);

  @Modifying
  @Query("update UserToken ut set ut.userTokenStatus = :status where ut.token = :token")
  void updateUserTokenStatusByToken(
      @Param("token") String token, @Param("status") UserTokenStatus status);

  @Modifying
  @Query("delete from UserToken ut where ut.userTokenStatus = :status")
  void deleteUserTokensByUserTokenStatus(@Param("status") UserTokenStatus status);
}
