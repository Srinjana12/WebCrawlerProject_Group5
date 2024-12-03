\*\*Web Crawler with Neo4j Integration\*\*  
Overview  
This project implements a web crawler that fetches and processes web pages, storing the data in a Neo4j database for analysis and visualization. The crawler collects page metadata, including titles and links, while also enabling graph-based insights such as PageRank, link density, and connectivity visualization.

Features  
Crawling Capabilities: Fetches web pages starting from a given URL up to a specified depth.  
Neo4j Integration: Stores pages and links as nodes and relationships in a graph database.

Graph Analytics:  
PageRank calculation.  
Identification of highly connected pages.  
Detection of orphan pages and dead ends.

Visualization: Graph-based visualization of page relationships, scaled by outgoing link counts.

Requirements  
Java: JDK 11 or later.  
Neo4j: Installed and running on localhost with Bolt protocol enabled.

Dependencies:  
Neo4j Java Driver  
Jsoup (HTML parsing)  
Apache Log4j (logging)

Setup  
Clone the repository.  
Configure Neo4j:  
Start the Neo4j server.

Set credentials   
Ensure neo4j.conf allows loading files:  
dbms.directories.import=/path/to/import/directory

Update connection details in the App.java file if necessary:

Build the project using Maven:  
mvn clean install

Run the application:

java \-jar target/web-crawler.jar

Input the starting URL and maximum crawl depth.

Analyze and visualize results in Neo4j.

Key Neo4j Queries

Page Storage

Nodes for pages:  
cypher  
MATCH (p:Page)  
RETURN p.url, p.title;

Links between pages:  
cypher  
MATCH (p1:Page)-\[:LINKS\_TO\]-\>(p2:Page)  
RETURN p1.url, p2.url;

Graph Analytics  
Most Outgoing Links:

cypher  
MATCH (p:Page)-\[:LINKS\_TO\]-\>()  
RETURN p.url, COUNT(\*) AS OutgoingLinks  
ORDER BY OutgoingLinks DESC;

PageRank:  
cypher  
CALL gds.pageRank.write({  
  nodeProjection: 'Page',  
  relationshipProjection: { LINKS\_TO: { type: 'LINKS\_TO' } },  
  writeProperty: 'pageRank'  
});

Dead Ends:  
cypher  
MATCH (p:Page)  
WHERE NOT (p)-\[:LINKS\_TO\]-\>()  
RETURN p.url;

Visualization  
Display the graph:  
cypher  
MATCH (p:Page)-\[r:LINKS\_TO\]-\>(q:Page)  
RETURN p, r, q;

Results  
Proportional visualization of outgoing links.  
Analysis of crawling performance, including pages crawled per second.  
Insights into the connectivity and structure of the crawled web.  
Contributions  
Contributions are welcome\! Fork the repository and submit a pull request with your enhancements.  
