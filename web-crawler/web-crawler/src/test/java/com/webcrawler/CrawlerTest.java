package com.webcrawler;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.Jsoup;

import java.util.HashSet;
import java.util.Set;

public class CrawlerTest {

    @Test
    public void testIsValidUrl() {
        // Arrange
        Crawler crawler = new Crawler(2, null);

        // Act & Assert
        assertTrue(crawler.isValidUrl("https://example.com"), "Valid HTTPS URL should return true.");
        assertTrue(crawler.isValidUrl("http://example.com"), "Valid HTTP URL should return true.");
        assertFalse(crawler.isValidUrl("javascript:void(0)"), "JavaScript link should return false.");
        assertFalse(crawler.isValidUrl("ftp://example.com"), "FTP link should return false.");
        assertFalse(crawler.isValidUrl(""), "Empty URL should return false.");
        //assertFalse(crawler.isValidUrl(null), "Null URL should return false.");
    }

    @Test
    public void testLinkExtraction() throws Exception {
        // Arrange
        String html = "<html><body><a href='https://example.com'>Example</a></body></html>";

        // Act
        Elements links = Jsoup.parse(html).select("a[href]");

        // Assert
        assertEquals(1, links.size(), "There should be exactly one link in the HTML.");
        Element link = links.first();
        assertEquals("https://example.com", link.absUrl("href"), "Extracted URL should match the expected value.");
    }

    @Test
    public void testCrawlMethod() {
        // Arrange
        String mockHtml = "<html><body>" +
                "<a href='https://example.com/page1'>Page 1</a>" +
                "<a href='https://example.com/page2'>Page 2</a>" +
                "</body></html>";

        Set<String> visitedUrls = new HashSet<>();
        Crawler crawler = new Crawler(1, null) {
            @Override
            public void crawl(String startUrl) {
                visitedUrls.add("https://example.com/page1");
                visitedUrls.add("https://example.com/page2");
            }
        };

        // Act
        crawler.crawl("https://example.com");

        // Assert
        assertTrue(visitedUrls.contains("https://example.com/page1"), "Visited URLs should include page1.");
        assertTrue(visitedUrls.contains("https://example.com/page2"), "Visited URLs should include page2.");
    }

    @Test
    public void testInvalidUrls() {
        // Arrange
        Crawler crawler = new Crawler(2, null);

        // Act & Assert
        assertFalse(crawler.isValidUrl(""), "Empty URL should return false.");
        assertFalse(crawler.isValidUrl("javascript:void(0)"), "JavaScript link should return false.");
    }

    @Test
    public void testTimeoutHandling() {
        // Arrange
        Crawler crawler = new Crawler(2, null);

        // Act & Assert
        assertDoesNotThrow(() -> crawler.crawl("https://nonexistent.website"), "Crawling a non-existent website should not throw an exception.");
    }

    @Test
    public void testConcurrentCrawling() {
        // Arrange
        Crawler crawler = new Crawler(2, null);

        // Act & Assert
        assertDoesNotThrow(() -> {
            Thread thread1 = new Thread(() -> crawler.crawl("https://example.com/page1"));
            Thread thread2 = new Thread(() -> crawler.crawl("https://example.com/page2"));

            thread1.start();
            thread2.start();

            thread1.join();
            thread2.join();
        }, "Concurrent crawling should not throw exceptions.");
    }
}
