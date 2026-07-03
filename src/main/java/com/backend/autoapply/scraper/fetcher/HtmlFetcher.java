package com.backend.autoapply.scraper.fetcher;

import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Interface for fetching HTML content from URLs.
 * Implementations can use different strategies: Jsoup, Playwright, Selenium, etc.
 */
public interface HtmlFetcher {

    /**
     * Fetch HTML content from the specified URL.
     * 
     * @param url The URL to fetch
     * @return Jsoup Document representing the HTML
     * @throws IOException if fetching fails
     */

    Document fetch(String url) throws IOException;

    /**
     * Get the name of the fetcher implementation.
     * 
     * @return Fetcher name (e.g., "Jsoup", "Playwright", "Selenium")
     */
    String getFetcherName();
}
