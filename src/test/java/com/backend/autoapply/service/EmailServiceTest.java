package com.backend.autoapply.service;

import com.backend.autoapply.model.ApplicationLog;
import com.backend.autoapply.model.JobOffer;
import com.backend.autoapply.repository.ApplicationLogRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
class EmailServiceTest {

    @Autowired
    private EmailService emailService;

    @MockBean
    private JavaMailSender mailSender;

    @MockBean
    private ApplicationLogRepository applicationLogRepository;

    @Test
    void testEmailServiceBeanExists() {
        assertNotNull(emailService);
    }

    @Test
    void testSendApplicationEmailWithMock() {
        JobOffer jobOffer = JobOffer.builder()
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

        when(mailSender.createMimeMessage()).thenReturn(any());
        when(applicationLogRepository.save(any(ApplicationLog.class))).thenReturn(new ApplicationLog());

        assertThrows(RuntimeException.class, () -> {
            emailService.sendApplicationEmail(jobOffer, "hr@techcorp.com");
        });

        verify(applicationLogRepository, atLeastOnce()).save(any(ApplicationLog.class));
    }
}
