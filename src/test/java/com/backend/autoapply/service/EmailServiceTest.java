package com.backend.autoapply.service;

import com.backend.autoapply.model.ApplicationLog;
import com.backend.autoapply.model.JobOffer;
import com.backend.autoapply.repository.ApplicationLogRepository;
import jakarta.mail.Session;
import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.time.LocalDateTime;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @MockitoBean
    private JavaMailSender mailSender;

    @MockitoBean
    private ApplicationLogRepository applicationLogRepository;

    @Test
    void testEmailServiceBeanExists() {
        assertNotNull(emailService);
    }

    @Test
    void testSendApplicationEmailSuccessfully() {

        JobOffer jobOffer = createMockJobOffer();

        MimeMessage mimeMessage = new MimeMessage((Session) null);

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        when(applicationLogRepository.save(any(ApplicationLog.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        assertDoesNotThrow(() ->
                emailService.sendApplicationEmail(jobOffer, "hr@techcorp.com"));

        verify(mailSender).send(mimeMessage);

        verify(applicationLogRepository).save(any(ApplicationLog.class));
    }

    @Test
    void testShouldSaveFailedLogWhenMailSendingFails() {

        JobOffer jobOffer = createMockJobOffer();

        MimeMessage mimeMessage = new MimeMessage((Session) null);

        when(mailSender.createMimeMessage()).thenReturn(mimeMessage);

        doThrow(new MailSendException("SMTP Error"))
                .when(mailSender)
                .send(any(MimeMessage.class));

        when(applicationLogRepository.save(any()))
                .thenAnswer(invocation -> invocation.getArgument(0));

        assertThrows(RuntimeException.class, () ->
                emailService.sendApplicationEmail(jobOffer, "hr@techcorp.com"));

        verify(applicationLogRepository).save(
                argThat(log ->
                        log.getStatus() == ApplicationLog.ApplicationStatus.FAILED
                )
        );
    }

    private JobOffer createMockJobOffer() {
        return  JobOffer.builder()
                .id(1L)
                .title("Software Engineer")
                .company("Tech Corp")
                .location("Remote")
                .url("https://example.com/job/1")
                .source("LinkedIn")
                .description("Great job opportunity")
                .salaryMin(50000.0)
                .salaryMax(70000.0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
}

