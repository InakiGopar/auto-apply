package com.backend.autoapply.repository;

import com.backend.autoapply.model.ApplicationLog;
import com.backend.autoapply.model.JobOffer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ApplicationLogRepositoryTest {

    @Autowired
    private ApplicationLogRepository applicationLogRepository;

    @Autowired
    private JobOfferRepository jobOfferRepository;

    @Test
    void testSaveAndFindById() {
        JobOffer jobOffer = JobOffer.builder()
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

        JobOffer savedJob = jobOfferRepository.save(jobOffer);

        ApplicationLog log = ApplicationLog.builder()
                .jobOffer(savedJob)
                .recipientEmail("hr@techcorp.com")
                .status(ApplicationLog.ApplicationStatus.SENT)
                .sentAt(LocalDateTime.now())
                .build();

        ApplicationLog saved = applicationLogRepository.save(log);
        assertNotNull(saved.getId());
        assertEquals("hr@techcorp.com", saved.getRecipientEmail());
    }

    @Test
    void testFindByJobOffer() {
        JobOffer jobOffer = JobOffer.builder()
                .title("Data Scientist")
                .company("Data Inc")
                .location("New York")
                .url("https://example.com/job/2")
                .source("Indeed")
                .description("Data science role")
                .salaryMin(60000.0)
                .salaryMax(80000.0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        JobOffer savedJob = jobOfferRepository.save(jobOffer);

        ApplicationLog log1 = ApplicationLog.builder()
                .jobOffer(savedJob)
                .recipientEmail("hr@datainc.com")
                .status(ApplicationLog.ApplicationStatus.SENT)
                .sentAt(LocalDateTime.now())
                .build();

        ApplicationLog log2 = ApplicationLog.builder()
                .jobOffer(savedJob)
                .recipientEmail("recruiter@datainc.com")
                .status(ApplicationLog.ApplicationStatus.FAILED)
                .errorMessage("SMTP error")
                .sentAt(LocalDateTime.now())
                .build();

        applicationLogRepository.save(log1);
        applicationLogRepository.save(log2);

        List<ApplicationLog> logs = applicationLogRepository.findByJobOfferId(savedJob.getId());
        assertEquals(2, logs.size());
    }
}
