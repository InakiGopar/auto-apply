package com.backend.autoapply.scraper.parser;

import com.backend.autoapply.model.JobOffer;
import org.jsoup.nodes.Document;

import java.util.List;

/**
 * Interface for parsing job offers from HTML documents.
 * Implementations define the specific parsing logic for each job board.
 */
public interface HtmlJobParser {

    /**
     * Parse job offers from the given HTML document.
     * 
     * @param document Jsoup Document containing the HTML
     * @return List of parsed job offers
     */
    List<JobOffer> parse(Document document);

    /**
     * Get the name of the parser implementation.
     * 
     * @return Parser name (e.g., "LinkedInParser", "IndeedParser")
     */
    String getParserName();
}
