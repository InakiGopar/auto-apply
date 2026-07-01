package com.backend.autoapply.service;

import com.backend.autoapply.model.JobOffer;
import com.backend.autoapply.repository.JobOfferRepository;
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
class JobOfferServiceTest {

    @Autowired
    private JobOfferService jobOfferService;

    @Test
    void testCreateAndRetrieveJobOffer() {
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

        JobOffer saved = jobOfferService.saveJobOffer(jobOffer);
        assertNotNull(saved.getId());
        assertEquals("Software Engineer", saved.getTitle());

        Optional<JobOffer> retrieved = jobOfferService.getJobOfferById(saved.getId());
        assertTrue(retrieved.isPresent());
        assertEquals("Tech Corp", retrieved.get().getCompany());
    }

    @Test
    void testCreateIfNotExists() {
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

        JobOffer first = jobOfferService.createIfNotExists(jobOffer);
        JobOffer second = jobOfferService.createIfNotExists(jobOffer);

        assertEquals(first.getId(), second.getId());
        assertEquals(1, jobOfferService.getAllJobOffers().stream()
                .filter(j -> j.getUrl().equals("https://example.com/job/2"))
                .count());
    }

    @Test
    void testMarkAsApplied() {
        JobOffer jobOffer = JobOffer.builder()
                .title("DevOps Engineer")
                .company("Cloud Co")
                .location("San Francisco")
                .url("https://example.com/job/3")
                .source("Glassdoor")
                .description("DevOps position")
                .salaryMin(70000.0)
                .salaryMax(90000.0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        JobOffer saved = jobOfferService.saveJobOffer(jobOffer);
        assertFalse(saved.getApplied());

        jobOfferService.markAsApplied(saved.getId());

        Optional<JobOffer> updated = jobOfferService.getJobOfferById(saved.getId());
        assertTrue(updated.isPresent());
        assertTrue(updated.get().getApplied());
    }

    @Test
    void testGetPendingApplications() {
        JobOffer pending1 = JobOffer.builder()
                .title("Frontend Developer")
                .company("Web Co")
                .location("Remote")
                .url("https://example.com/job/4")
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
                .url("https://example.com/job/5")
                .source("LinkedIn")
                .description("Backend role")
                .salaryMin(60000.0)
                .salaryMax(80000.0)
                .applied(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        jobOfferService.saveJobOffer(pending1);
        jobOfferService.saveJobOffer(applied);

        List<JobOffer> pending = jobOfferService.getPendingApplications();
        assertEquals(1, pending.size());
        assertEquals("Frontend Developer", pending.get(0).getTitle());
    }

    @Test
    void testGetJobOfferByUrl() {
        JobOffer jobOffer = JobOffer.builder()
                .title("Full Stack Developer")
                .company("Full Stack Inc")
                .location("Chicago")
                .url("https://example.com/job/6")
                .source("Indeed")
                .description("Full stack role")
                .salaryMin(65000.0)
                .salaryMax(85000.0)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        jobOfferService.saveJobOffer(jobOffer);

        Optional<JobOffer> found = jobOfferService.getJobOfferByUrl("https://example.com/job/6");
        assertTrue(found.isPresent());
        assertEquals("Full Stack Developer", found.get().getTitle());
    }
}
