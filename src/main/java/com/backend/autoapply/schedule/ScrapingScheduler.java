package com.backend.autoapply.schedule;

import com.backend.autoapply.model.JobOffer;
import com.backend.autoapply.service.ScraperService;
import com.backend.autoapply.service.JobOfferService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ScrapingScheduler {

    private final ScraperService scraperService;
    private final JobOfferService jobOfferService;

    @Scheduled(cron = "${app.scheduling.scraping.cron:0 0 */6 * * *}")
    public void scheduleScraping() {
        log.info("Starting scheduled scraping job");
        try {
            scraperService.runAllScrapers();
            log.info("Scheduled scraping job completed successfully");
        } catch (Exception e) {
            log.error("Error during scheduled scraping", e);
        }
    }

    @Scheduled(cron = "${app.scheduling.application.cron:0 0 */12 * * *}")
    public void scheduleApplications() {
        log.info("Starting scheduled application job");
        try {
            var pendingJobs = jobOfferService.getPendingApplications();
            log.info("Found {} pending job applications", pendingJobs.size());

            for (var job : pendingJobs) {
                try {
                    String recipientEmail = determineRecipientEmail(job);
                    jobOfferService.applyToJobOffer(job.getId(), recipientEmail);
                    log.info("Applied to job: {}", job.getTitle());
                } catch (Exception e) {
                    log.error("Failed to apply to job: {}", job.getTitle(), e);
                }
            }

            log.info("Scheduled application job completed");
        } catch (Exception e) {
            log.error("Error during scheduled applications", e);
        }
    }

    private String determineRecipientEmail(JobOffer job) {
        return "hr@" + job.getCompany().toLowerCase().replace(" ", "") + ".com";
    }
}
