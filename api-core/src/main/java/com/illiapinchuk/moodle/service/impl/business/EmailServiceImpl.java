package com.illiapinchuk.moodle.service.impl.business;

import com.illiapinchuk.moodle.model.dto.EmailDto;
import com.illiapinchuk.moodle.service.business.EmailService;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/** Implementation of the EmailService interface for sending emails using JavaMailSender. */
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

  private final JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String supportEmail;

  @Override
  public void sendEmail(@Nonnull final EmailDto emailDto) {
    final var emailLetter = new SimpleMailMessage();
    emailLetter.setFrom(supportEmail);
    emailLetter.setTo(emailDto.getTo());
    emailLetter.setSubject(emailDto.getSubject());
    emailLetter.setText(emailDto.getContent());

    mailSender.send(emailLetter);
  }
}
