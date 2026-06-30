package com.backend.autoapply.scraper;

import com.backend.autoapply.model.JobOffer;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public abstract class BaseScraper implements JobScraper {

    protected Document fetchDocument(String url) throws IOException {
        return Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36")
                .timeout(30000)
                .get();
    }

    protected String safeText(Element element, String selector) {
        Element selected = element.selectFirst(selector);
        return selected != null ? selected.text().trim() : "";
    }

    protected String safeAttr(Element element, String selector, String attr) {
        Element selected = element.selectFirst(selector);
        return selected != null ? selected.attr(attr).trim() : "";
    }

    protected List<JobOffer> extractJobOffers(Document document, String jobCardSelector) {
        List<JobOffer> jobOffers = new ArrayList<>();
        Elements jobCards = document.select(jobCardSelector);

        for (Element card : jobCards) {
            try {
                JobOffer jobOffer = extractJobOfferFromCard(card);
                if (jobOffer != null) {
                    jobOffers.add(jobOffer);
                }
            } catch (Exception e) {
                log.error("Error extracting job offer from card", e);
            }
        }

        return jobOffers;
    }

    protected abstract JobOffer extractJobOfferFromCard(Element card);

    protected JobOffer.JobOfferBuilder baseJobOfferBuilder() {
        return JobOffer.builder()
                .source(getSourceName())
                .matchesProfile(true)
                .applied(false)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now());
    }
}
