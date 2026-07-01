package com.backend.autoapply.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ApplicationLogTest {

    @Test
    void testBuilder() {
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
                .build();

        ApplicationLog log = ApplicationLog.builder()
                .id(1L)
                .jobOffer(jobOffer)
                .recipientEmail("hr@techcorp.com")
                .status(ApplicationLog.ApplicationStatus.SENT)
                .errorMessage(null)
                .sentAt(LocalDateTime.now())
                .build();

        assertNotNull(log);
        assertEquals(1L, log.getId());
        assertEquals("hr@techcorp.com", log.getRecipientEmail());
        assertEquals(ApplicationLog.ApplicationStatus.SENT, log.getStatus());
        assertNull(log.getErrorMessage());
    }

    @Test
    void testApplicationStatusEnum() {
        assertEquals(ApplicationLog.ApplicationStatus.SENT, ApplicationLog.ApplicationStatus.valueOf("SENT"));
        assertEquals(ApplicationLog.ApplicationStatus.FAILED, ApplicationLog.ApplicationStatus.valueOf("FAILED"));
    }

    @Test
    void testBuilderWithFailedStatus() {
        JobOffer jobOffer = JobOffer.builder()
                .id(1L)
                .title("Data Scientist")
                .company("Data Inc")
                .location("New York")
                .url("https://example.com/job/2")
                .source("Indeed")
                .description("Data science role")
                .salaryMin(60000.0)
                .salaryMax(80000.0)
                .build();

        ApplicationLog log = ApplicationLog.builder()
                .jobOffer(jobOffer)
                .recipientEmail("hr@datainc.com")
                .status(ApplicationLog.ApplicationStatus.FAILED)
                .errorMessage("SMTP connection failed")
                .sentAt(LocalDateTime.now())
                .build();

        assertEquals(ApplicationLog.ApplicationStatus.FAILED, log.getStatus());
        assertEquals("SMTP connection failed", log.getErrorMessage());
    }

    @Test
    void testNoArgsConstructor() {
        ApplicationLog log = new ApplicationLog();
        assertNotNull(log);
    }
}
