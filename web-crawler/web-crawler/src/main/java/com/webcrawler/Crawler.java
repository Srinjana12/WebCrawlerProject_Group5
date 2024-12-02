package com.webcrawler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Crawler {
    private static final Logger logger = LogManager.getLogger(Crawler.class);
    private final int maxDepth;
    private final CustomGraphDatabase graphDb;

    public Crawler(int maxDepth, CustomGraphDatabase graphDb) {
        this.maxDepth = maxDepth;
        this.graphDb = graphDb;
    }

    public void crawl(String startUrl) {
        Queue<UrlDepthPair> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();
        int pageCount = 0; // Counter for pages crawled

        try (FileWriter csvWriter = new FileWriter("crawl_results.csv")) {
            // Write CSV header
            csvWriter.append("URL,Depth,Title,Database Write Time (ms)\n");

            queue.add(new UrlDepthPair(startUrl, 0));
            logger.info("Starting crawl for URL: {}", startUrl);

            long startTime = System.nanoTime();

            while (!queue.isEmpty()) {
                UrlDepthPair current = queue.poll();
                String url = current.url;
                int depth = current.depth;

                if (visited.contains(url)) {
                    logger.debug("Skipping already visited URL: {}", url);
                    continue;
                }

                if (depth > maxDepth) {
                    logger.debug("Skipping URL due to depth limit: {}", url);
                    continue;
                }

                visited.add(url);
                pageCount++;
                logger.info("Crawling: {} (Depth: {})", url, depth);

                try {
                    long dbStartTime = System.nanoTime();

                    Document doc = Jsoup.connect(url)
                            .userAgent("Mozilla/5.0")
                            .timeout(10000)
                            .get();

                    String title = doc.title();
                    logger.info("Title of {}: {}", url, title);

                    if (graphDb != null) {
                        graphDb.savePage(url, title);
                    }

                    long dbEndTime = System.nanoTime();
                    long dbWriteTime = TimeUnit.NANOSECONDS.toMillis(dbEndTime - dbStartTime);
                    logger.info("Database write time: {} ms", dbWriteTime);

                    // Write crawl data to CSV
                    csvWriter.append(String.format("\"%s\",%d,\"%s\",%d\n", url, depth, title.replace("\"", ""), dbWriteTime));
                    csvWriter.flush(); // Ensure the data is written immediately to the CSV file

                    Elements links = doc.select("a[href]");
                    for (org.jsoup.nodes.Element link : links) {
                        String absUrl = link.absUrl("href");
                        if (isValidUrl(absUrl) && !visited.contains(absUrl)) {
                            queue.add(new UrlDepthPair(absUrl, depth + 1));
                            logger.debug("Found valid link: {}", absUrl);
                            if (graphDb != null) {
                                graphDb.saveLink(url, absUrl);
                            }
                        }
                    }
                } catch (Exception e) {
                    logger.error("Failed to crawl {}: {}", url, e.getMessage());
                }
            }

            long endTime = System.nanoTime();
            long elapsedTimeMillis = TimeUnit.NANOSECONDS.toMillis(endTime - startTime);
            double totalTimeInSeconds = elapsedTimeMillis / 1000.0;
            double pagesPerSecond = pageCount / totalTimeInSeconds;

            logger.info("Crawling completed for start URL: {}", startUrl);
            logger.info("Total pages crawled: {}", pageCount);
            logger.info("Crawling speed: {} pages/second", pagesPerSecond);

            // Write final statistics to CSV
            csvWriter.append(String.format("\nTotal Pages Crawled,%d\n", pageCount));
            csvWriter.append(String.format("Crawling Speed,%f pages/second\n", pagesPerSecond));

        } catch (IOException e) {
            logger.error("Error writing to CSV file: {}", e.getMessage());
        }
    }

    
}
