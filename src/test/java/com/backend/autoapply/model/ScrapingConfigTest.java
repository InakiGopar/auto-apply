package com.backend.autoapply.model;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class ScrapingConfigTest {

    @Test
    void testBuilder() {
        ScrapingConfig config = ScrapingConfig.builder()
                .id(1L)
                .name("LinkedIn Scraper")
                .baseUrl("https://linkedin.com")
                .searchPath("/jobs")
                .active(true)
                .cronHours(24)
                .lastRun(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        assertNotNull(config);
        assertEquals(1L, config.getId());
        assertEquals("LinkedIn Scraper", config.getName());
        assertEquals("https://linkedin.com", config.getBaseUrl());
        assertEquals("/jobs", config.getSearchPath());
        assertTrue(config.getActive());
        assertEquals(24, config.getCronHours());
    }

    @Test
    void testDefaultValues() {
        ScrapingConfig config = ScrapingConfig.builder()
                .name("Indeed Scraper")
                .baseUrl("https://indeed.com")
                .searchPath("/jobs")
                .lastRun(LocalDateTime.now())
                .build();

        assertTrue(config.getActive());
        assertEquals(24, config.getCronHours());
        assertNotNull(config.getCreatedAt());
        assertNotNull(config.getUpdatedAt());
    }

    @Test
    void testPreUpdate() {
        ScrapingConfig config = ScrapingConfig.builder()
                .name("Glassdoor Scraper")
                .baseUrl("https://glassdoor.com")
                .searchPath("/jobs")
                .lastRun(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        LocalDateTime oldUpdatedAt = config.getUpdatedAt();
        
        config.preUpdate();
        
        assertNotNull(config.getUpdatedAt());
        assertTrue(config.getUpdatedAt().isAfter(oldUpdatedAt) || config.getUpdatedAt().isEqual(oldUpdatedAt));
    }

    @Test
    void testAllArgsConstructor() {
        LocalDateTime now = LocalDateTime.now();
        ScrapingConfig config = new ScrapingConfig(
                1L,
                "Stack Overflow Scraper",
                "https://stackoverflow.com",
                "/jobs",
                true,
                12,
                now,
                now,
                now
        );

        assertEquals(1L, config.getId());
        assertEquals("Stack Overflow Scraper", config.getName());
        assertEquals("https://stackoverflow.com", config.getBaseUrl());
    }

    @Test
    void testNoArgsConstructor() {
        ScrapingConfig config = new ScrapingConfig();
        assertNotNull(config);
    }
}
