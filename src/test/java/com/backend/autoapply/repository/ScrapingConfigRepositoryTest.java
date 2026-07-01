package com.backend.autoapply.repository;

import com.backend.autoapply.model.ScrapingConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ScrapingConfigRepositoryTest {

    @Autowired
    private ScrapingConfigRepository scrapingConfigRepository;

    @Test
    void testSaveAndFindById() {
        ScrapingConfig config = ScrapingConfig.builder()
                .name("LinkedIn Scraper")
                .baseUrl("https://linkedin.com")
                .searchPath("/jobs")
                .active(true)
                .cronHours(24)
                .lastRun(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        ScrapingConfig saved = scrapingConfigRepository.save(config);
        assertNotNull(saved.getId());
        assertEquals("LinkedIn Scraper", saved.getName());
    }

    @Test
    void testFindByName() {
        ScrapingConfig config = ScrapingConfig.builder()
                .name("Indeed Scraper")
                .baseUrl("https://indeed.com")
                .searchPath("/jobs")
                .active(true)
                .cronHours(12)
                .lastRun(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        scrapingConfigRepository.save(config);

        Optional<ScrapingConfig> found = scrapingConfigRepository.findByName("Indeed Scraper");
        assertTrue(found.isPresent());
        assertEquals("Indeed Scraper", found.get().getName());
    }

    @Test
    void testNameUniqueConstraint() {
        ScrapingConfig config1 = ScrapingConfig.builder()
                .name("Glassdoor Scraper")
                .baseUrl("https://glassdoor.com")
                .searchPath("/jobs")
                .active(true)
                .cronHours(6)
                .lastRun(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        scrapingConfigRepository.save(config1);

        ScrapingConfig config2 = ScrapingConfig.builder()
                .name("Glassdoor Scraper")
                .baseUrl("https://glassdoor.com/v2")
                .searchPath("/jobs")
                .active(false)
                .cronHours(8)
                .lastRun(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        assertThrows(Exception.class, () -> {
            scrapingConfigRepository.save(config2);
        });
    }
}
