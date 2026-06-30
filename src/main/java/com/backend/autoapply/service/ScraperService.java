package com.backend.autoapply.service;

import com.backend.autoapply.model.JobOffer;
import com.backend.autoapply.model.ScrapingConfig;
import com.backend.autoapply.repository.ScrapingConfigRepository;
import com.backend.autoapply.scraper.JobScraper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScraperService {

    private final List<JobScraper> scrapers;
    private final JobOfferService jobOfferService;
    private final ScrapingConfigRepository scrapingConfigRepository;

    public void runAllScrapers() {
        Map<String, JobScraper> scraperMap = scrapers.stream()
                .collect(Collectors.toMap(JobScraper::getSourceName, scraper -> scraper));

        List<ScrapingConfig> activeConfigs = scrapingConfigRepository.findByActiveTrue();

        for (ScrapingConfig config : activeConfigs) {
            try {
                JobScraper scraper = scraperMap.get(config.getName());
                if (scraper != null && scraper.isActive()) {
                    log.info("Running scraper for: {}", config.getName());
                    runScraper(scraper, config);
                } else {
                    log.warn("Scraper not found or inactive for: {}", config.getName());
                }
            } catch (Exception e) {
                log.error("Error running scraper for: {}", config.getName(), e);
            }
        }
    }

    public void runScraper(JobScraper scraper, ScrapingConfig config) {
        String searchUrl = config.getBaseUrl() + config.getSearchPath();
        log.info("Scraping URL: {}", searchUrl);

        List<JobOffer> jobOffers = scraper.scrapeJobOffers(searchUrl);

        log.info("Found {} job offers from {}", jobOffers.size(), scraper.getSourceName());

        int newOffers = 0;
        for (JobOffer offer : jobOffers) {
            JobOffer saved = jobOfferService.createIfNotExists(offer);
            if (saved.getCreatedAt().isEqual(offer.getCreatedAt())) {
                newOffers++;
            }
        }

        log.info("Saved {} new job offers from {}", newOffers, scraper.getSourceName());

        config.setLastRun(LocalDateTime.now());
        scrapingConfigRepository.save(config);
    }

    public List<JobScraper> getActiveScrapers() {
        return scrapers.stream()
                .filter(JobScraper::isActive)
                .toList();
    }
}
