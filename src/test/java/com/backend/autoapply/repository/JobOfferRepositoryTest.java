package com.backend.autoapply.repository;

import com.backend.autoapply.model.JobOffer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class JobOfferRepositoryTest {

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

        JobOffer saved = jobOfferRepository.save(jobOffer);
        assertNotNull(saved.getId());

        Optional<JobOffer> found = jobOfferRepository.findById(saved.getId());
        assertTrue(found.isPresent());
        assertEquals("Software Engineer", found.get().getTitle());
    }

    @Test
    void testFindByUrl() {
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

        jobOfferRepository.save(jobOffer);

        Optional<JobOffer> found = jobOfferRepository.findByUrl("https://example.com/job/2");
        assertTrue(found.isPresent());
        assertEquals("Data Scientist", found.get().getTitle());
    }

    @Test
    void testFindPendingApplications() {
        JobOffer pending = JobOffer.builder()
                .title("Frontend Developer")
                .company("Web Co")
                .location("Remote")
                .url("https://example.com/job/3")
                .source("LinkedIn")
                .description("Frontend role")
                .salaryMin(55000.0)
                .salaryMax(75000.0)
                .applied(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        JobOffer applied = JobOffer.builder()
                .title("Backend Developer")
                .company("Server Co")
                .location("Remote")
                .url("https://example.com/job/4")
                .source("LinkedIn")
                .description("Backend role")
                .salaryMin(60000.0)
                .salaryMax(80000.0)
                .applied(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        jobOfferRepository.save(pending);
        jobOfferRepository.save(applied);

        List<JobOffer> pendingJobs = jobOfferRepository.findPendingApplications();
        assertEquals(1, pendingJobs.size());
        assertEquals("Frontend Developer", pendingJobs.get(0).getTitle());
    }

    @Test
    void testUrlUniqueConstraint() {
        JobOffer jobOffer1 = JobOffer.builder()
                .title("Full Stack Developer")
                .company("Full Stack Inc")
                .location("Chicago")
                .url("https://example.com/job/5")
                .source("Indeed")
                .description("Full stack role")
                .salaryMin(65000.0)
                .salaryMax(85000.0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        jobOfferRepository.save(jobOffer1);

        JobOffer jobOffer2 = JobOffer.builder()
                .title("Another Developer")
                .company("Another Corp")
                .location("Boston")
                .url("https://example.com/job/5")
                .source("LinkedIn")
                .description("Another role")
                .salaryMin(70000.0)
                .salaryMax(90000.0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        assertThrows(Exception.class, () -> {
            jobOfferRepository.save(jobOffer2);
        });
    }
}
