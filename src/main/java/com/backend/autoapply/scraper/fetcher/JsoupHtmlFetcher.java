package com.backend.autoapply.scraper.fetcher;

import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Jsoup-based implementation of HtmlFetcher.
 * Suitable for static HTML pages without dynamic JavaScript content.
 */
@Slf4j
public class JsoupHtmlFetcher implements HtmlFetcher {

    private static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36";
    private static final int TIMEOUT = 30000;

    @Override
    public Document fetch(String url) throws IOException {
        log.debug("Fetching HTML from: {}", url);
        //send the http request and return the document (the html content)
        return Jsoup.connect(url)
                .userAgent(USER_AGENT)
                .timeout(TIMEOUT)
                .get();
    }

    @Override
    public String getFetcherName() {
        return "Jsoup";
    }
}
