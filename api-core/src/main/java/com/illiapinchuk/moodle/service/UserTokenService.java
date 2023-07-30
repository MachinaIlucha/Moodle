package com.illiapinchuk.moodle.service;

import com.illiapinchuk.moodle.persistence.entity.User;
import com.illiapinchuk.moodle.persistence.entity.UserToken;

/** Service interface for managing UserToken entities. */
public interface UserTokenService {
  void saveToken(UserToken userToken);

  void deleteTokenByUserId(Long userId);

  User getUserByToken(String token);
}
