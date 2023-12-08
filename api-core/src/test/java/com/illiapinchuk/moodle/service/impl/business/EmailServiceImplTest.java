package com.illiapinchuk.moodle.service.impl.business;

import com.illiapinchuk.moodle.common.TestConstants;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class EmailServiceImplTest {

  @InjectMocks private EmailServiceImpl emailService;

  @Mock private JavaMailSender mailSender;

  @Test
  void sendEmailShouldCallMailSenderSend() {
    final var emailLetter = new SimpleMailMessage();
    emailLetter.setFrom("support@mail.com");
    emailLetter.setTo(TestConstants.EmailConstants.VALID_EMAIL_DTO.getTo());
    emailLetter.setSubject(TestConstants.EmailConstants.VALID_EMAIL_DTO.getSubject());
    emailLetter.setText(TestConstants.EmailConstants.VALID_EMAIL_DTO.getContent());

    doNothing().when(mailSender).send(any(SimpleMailMessage.class));

    emailService.sendEmail(TestConstants.EmailConstants.VALID_EMAIL_DTO);

    verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
  }
}
