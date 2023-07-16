package com.illiapinchuk.moodle.service;

/** Service to reset password. */
public interface PasswordRecoveryService {

  void sendPasswordRecoveryEmailWithToken(String userEmail, String scheme, String serverName, String serverPort);
}
