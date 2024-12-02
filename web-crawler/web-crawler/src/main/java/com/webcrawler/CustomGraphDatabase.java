package com.webcrawler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.driver.*;

public class CustomGraphDatabase implements AutoCloseable {
    private static final Logger logger = LogManager.getLogger(CustomGraphDatabase.class);
    private final Driver driver;

    public CustomGraphDatabase(String uri, String username, String password) {
        logger.info("Connecting to Neo4j database at URI: {}", uri);
        this.driver = GraphDatabase.driver(uri, AuthTokens.basic(username, password));
    }

    public void savePage(String url, String title) {
        try (Session session = driver.session()) {
            logger.debug("Saving page: URL={}, Title={}", url, title);
            session.writeTransaction(tx -> {
                tx.run("MERGE (p:Page {url: $url, title: $title})",
                        Values.parameters("url", url, "title", title));
                return null;
            });
            logger.info("Page saved successfully: {}", url);
        } catch (Exception e) {
            logger.error("Failed to save page {}: {}", url, e.getMessage());
        }
    }
