package com.backend.autoapply.scraper;

import com.backend.autoapply.model.JobOffer;
import com.backend.autoapply.scraper.fetcher.HtmlFetcher;
import com.backend.autoapply.scraper.parser.HtmlJobParser;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;

@Slf4j
public abstract class BaseScraper implements JobScraper {

    private final HtmlFetcher htmlFetcher;
    private final HtmlJobParser htmlJobParser;

    protected BaseScraper(HtmlFetcher htmlFetcher, HtmlJobParser htmlJobParser) {
        this.htmlFetcher = htmlFetcher;
        this.htmlJobParser = htmlJobParser;
    }

    @Override
    public List<JobOffer> scrapeJobOffers(String searchQuery) {
        //build url
        String url = buildSearchUrl(searchQuery);
        
        try {
            log.debug("Scraping from: {} using fetcher: {} and parser: {}", 
                    url, htmlFetcher.getFetcherName(), htmlJobParser.getParserName());
            //get the html
            Document document = htmlFetcher.fetch(url);
            // parse/extract job offers
            return htmlJobParser.parse(document);
        } catch (IOException e) {
            logError("Error scraping job offers from: " + url, e);
            return List.of();
        }
    }

    /**
     * Build the search URL for the specific job board.
     * 
     * @param searchQuery The search query (e.g., job title, keywords)
     * @return Complete URL to scrape
     */
    protected abstract String buildSearchUrl(String searchQuery);

    protected JobOffer.JobOfferBuilder baseJobOfferBuilder() {
        return JobOffer.builder()
                .source(getSourceName())
                .matchesProfile(true)
                .applied(false)
                .createdAt(java.time.LocalDateTime.now())
                .updatedAt(java.time.LocalDateTime.now());
    }

    protected void logError(String message, Exception e) {
        log.error(message, e);
    }
}
