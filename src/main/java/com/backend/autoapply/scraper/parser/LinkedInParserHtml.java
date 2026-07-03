package com.backend.autoapply.scraper.parser;

import com.backend.autoapply.model.JobOffer;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

/**
 * Example implementation of JobOfferParser for LinkedIn job listings.
 * This demonstrates how to implement a parser for a specific job board.
 */
@Slf4j
public class LinkedInParserHtml implements HtmlJobParser {

    private static final String JOB_CARD_SELECTOR = ".job-card-container";
    private static final String TITLE_SELECTOR = ".job-card-list__title";
    private static final String COMPANY_SELECTOR = ".job-card-container__company-name";
    private static final String LOCATION_SELECTOR = ".job-card-container__metadata-item";
    private static final String URL_SELECTOR = ".job-card-list__title";

    @Override
    public List<JobOffer> parse(Document document) {
        List<JobOffer> jobOffers = new ArrayList<>();
        Elements jobCards = document.select(JOB_CARD_SELECTOR);

        log.debug("Found {} job cards to parse", jobCards.size());

        for (Element card : jobCards) {
            try {
                JobOffer jobOffer = extractJobOffer(card);
                if (jobOffer != null) {
                    jobOffers.add(jobOffer);
                }
            } catch (Exception e) {
                log.error("Error parsing job card", e);
            }
        }

        return jobOffers;
    }

    private JobOffer extractJobOffer(Element card) {
        String title = safeText(card, TITLE_SELECTOR);
        String company = safeText(card, COMPANY_SELECTOR);
        String location = safeText(card, LOCATION_SELECTOR);
        String url = safeAttr(card, URL_SELECTOR, "href");

        if (title.isEmpty()) {
            return null;
        }

        return JobOffer.builder()
                .title(title)
                .company(company)
                .location(location)
                .url(url.startsWith("http") ? url : "https://www.linkedin.com" + url)
                .build();
    }

    private String safeText(Element element, String selector) {
        Element selected = element.selectFirst(selector);
        return selected != null ? selected.text().trim() : "";
    }

    private String safeAttr(Element element, String selector, String attr) {
        Element selected = element.selectFirst(selector);
        return selected != null ? selected.attr(attr).trim() : "";
    }

    @Override
    public String getParserName() {
        return "LinkedInParser";
    }
}
