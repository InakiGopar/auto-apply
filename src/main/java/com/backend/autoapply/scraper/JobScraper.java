package com.backend.autoapply.scraper;

import com.backend.autoapply.model.JobOffer;

import java.util.List;

public interface JobScraper {

    String getSourceName();

    List<JobOffer> scrapeJobOffers(String searchQuery);

    default boolean isActive() {
        return true;
    }
}
