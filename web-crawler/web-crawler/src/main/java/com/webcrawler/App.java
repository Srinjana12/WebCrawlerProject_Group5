package com.webcrawler;

import java.util.Scanner;

public class App {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter the starting URL: ");
        String startUrl = scanner.nextLine();

        System.out.print("Enter the maximum crawl depth: ");
        int maxDepth = scanner.nextInt();

        // Neo4j database credentials
        String uri = "bolt://localhost:7687";
        String user = "neo4j";
        String password = "teamalgo";

        try (CustomGraphDatabase graphDb = new CustomGraphDatabase(uri, user, password)) {
            Crawler crawler = new Crawler(maxDepth, graphDb);
            Benchmark benchmark = new Benchmark("Crawling Benchmark");

            LoggingUtility.info(App.class, "Starting crawl...");
            benchmark.start();

            crawler.crawl(startUrl);

            benchmark.stop();
            benchmark.printResults();

            LoggingUtility.info(App.class, "Crawling completed.");
        }

    }
}
