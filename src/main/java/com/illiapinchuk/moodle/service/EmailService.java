package com.illiapinchuk.moodle.service;

import com.illiapinchuk.moodle.model.dto.EmailDto;

/** The EmailService interface provides methods for sending emails. */
public interface EmailService {
  void sendEmail(EmailDto emailDto);
}
