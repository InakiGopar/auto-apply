package com.backend.autoapply.scraper;

import com.backend.autoapply.scraper.fetcher.JsoupHtmlFetcher;
import com.backend.autoapply.scraper.parser.LinkedInParserHtml;
import org.springframework.stereotype.Component;

/**
 * Example implementation of a job scraper for LinkedIn.
 * Uses Jsoup for HTML fetching and LinkedInParser for parsing.
 */
@Component
public class LinkedInScraper extends BaseScraper {

    private static final String BASE_URL = "https://www.linkedin.com/jobs/search/";

    public LinkedInScraper() {
        super(new JsoupHtmlFetcher(), new LinkedInParserHtml());
    }

    @Override
    protected String buildSearchUrl(String searchQuery) {
        return BASE_URL + "?keywords=" + searchQuery.replace(" ", "%20");
    }

    @Override
    public String getSourceName() {
        return "LinkedIn";
    }
}
