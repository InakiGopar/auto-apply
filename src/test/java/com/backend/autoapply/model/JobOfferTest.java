package com.backend.autoapply.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class JobOfferTest {

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
                .applied(false)
                .matchesProfile(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        assertNotNull(jobOffer);
        assertEquals("Software Engineer", jobOffer.getTitle());
        assertEquals("Tech Corp", jobOffer.getCompany());
        assertEquals("Remote", jobOffer.getLocation());
        assertEquals("https://example.com/job/1", jobOffer.getUrl());
        assertEquals("LinkedIn", jobOffer.getSource());
        assertEquals(50000.0, jobOffer.getSalaryMin());
        assertEquals(70000.0, jobOffer.getSalaryMax());
        assertFalse(jobOffer.getApplied());
        assertTrue(jobOffer.getMatchesProfile());
    }

    @Test
    void testDefaultValues() {
        JobOffer jobOffer = JobOffer.builder()
                .title("Data Scientist")
                .company("Data Inc")
                .location("New York")
                .url("https://example.com/job/2")
                .source("Indeed")
                .description("Data science role")
                .salaryMin(60000.0)
                .salaryMax(80000.0)
                .build();

        assertFalse(jobOffer.getApplied());
        assertTrue(jobOffer.getMatchesProfile());
        assertNotNull(jobOffer.getCreatedAt());
        assertNotNull(jobOffer.getUpdatedAt());
    }

    @Test
    void testPreUpdate() {
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

        LocalDateTime oldUpdatedAt = jobOffer.getUpdatedAt();
        
        jobOffer.preUpdate();
        
        assertNotNull(jobOffer.getUpdatedAt());
        assertTrue(jobOffer.getUpdatedAt().isAfter(oldUpdatedAt) || jobOffer.getUpdatedAt().isEqual(oldUpdatedAt));
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        JobOffer jobOffer = new JobOffer(
                1L,
                "Full Stack Developer",
                "Full Stack Inc",
                "Chicago",
                "https://example.com/job/4",
                "Indeed",
                "Full stack role",
                65000.0,
                85000.0,
                false,
                true,
                now,
                now
        );

        assertEquals(1L, jobOffer.getId());
        assertEquals("Full Stack Developer", jobOffer.getTitle());
        assertEquals("Full Stack Inc", jobOffer.getCompany());
    }

    @Test
    void testNoArgsConstructor() {
        JobOffer jobOffer = new JobOffer();
        assertNotNull(jobOffer);
    }
}
