package com.davidemarino;

import java.net.URI;
import java.util.*;
import java.util.concurrent.*;

/**
 *
 */
public class Crawler {
    private LinkedHashSet<String> processedUrls;
    private LinkedHashSet<String> toBeProcessedUrls;
    private LinkedHashSet<String> processingUrls;

    private ExecutorService executorService;
    private List<Link> links;

    private URI basePath;
    private int threadSize;
    private int stopOverNumberOfLinks;

    /**
     * Private constructor. It takes the parameters passed from the command line to init itself
     *
     * @param startingUrl
     * @param threadSize
     * @param stopOverNumberOfLinks
     */
    private Crawler(String startingUrl, int threadSize, int stopOverNumberOfLinks) {
        this.threadSize = threadSize;
        this.stopOverNumberOfLinks = stopOverNumberOfLinks;
        basePath = URI.create(startingUrl).resolve("/");
        links = new ArrayList<>();

        processedUrls = new LinkedHashSet<>();
        toBeProcessedUrls = new LinkedHashSet<>();
        toBeProcessedUrls.add(startingUrl);
        processingUrls = new LinkedHashSet<>();
        executorService = Executors.newFixedThreadPool(threadSize);
    }

    /**
     * The only public static method.
     * It takes a startingPage, and it init a new Crawler to download all requested links
     *
     * @param startingUrl the starting url
     * @param threadSize the number of threads to be used
     * @param stopOverNumberOfLinks the number of links that when reached no more pages will be downloaded
     * @return the requested links
     */
    public static List<Link> downloadLinks(String startingUrl, int threadSize, int stopOverNumberOfLinks) {
        Crawler crawler = new Crawler(startingUrl, threadSize, stopOverNumberOfLinks);
        return crawler.downloadLinks();
    }

    /**
     * Starting method to be executed to start downloading content and urls
     * @return the list of requested urls
     */
    private List<Link> downloadLinks() {
        for (int i = 0; i < threadSize; i++) {
            executorService.submit(() -> crawl());
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return links;
    }


    /**
     * This method is an infinite loop executed by each working thread.
     * At the beginning it tests if there are urls to be processed. If none is available it exits.
     */
    private void crawl() {
        while (true) {
            String pageUrl = getNextUrl();
            if (pageUrl == null) {
                return;
            }

            String content = Downloader.downloadContent(pageUrl);
            List<Link> extractedLinks = LinksExtractor.extractLinks(basePath, pageUrl, content);
            manageLinks(extractedLinks, pageUrl);
            synchronized (this) {
                notifyAll();
            }
            printStats();
        }
    }

    /**
     * Add the list of extracted links from the current page to the links already collected.
     * It adds all the links in the set of toBeProcessedUrls if they were not already elaborated.
     * It moves also the pageUrl link from the processingUrls to the processedUrls.
     *
     * @param extractedLinks the links extracted from a page
     * @param pageUrl the page containing the extracted links
     */
    private synchronized void manageLinks(List<Link> extractedLinks, String pageUrl) {
        links.addAll(extractedLinks);
        for (Link link : extractedLinks) {
            if (!processedUrls.contains(link.getUrl()) && !processingUrls.contains(link.getUrl())) {
                toBeProcessedUrls.add(link.getUrl());
            }
        }
        processingUrls.remove(pageUrl);
        processedUrls.add(pageUrl);
    }

    /**
     * This method take the first url to be visited from the toBeProcessedUrls set and move it to the processingUrls.
     * If no url can be visited and there are processing urls it wait.
     * If no url can be visited and there are no processing urls it returns null,
     *
     * The method is synchronized because can be invoked by different threads
     *
     * @return the url to be visited or null if no url is available
     */
    private synchronized String getNextUrl() {
        while (true) {
            if (links.size() > stopOverNumberOfLinks) {
                return null;
            }
            if (!toBeProcessedUrls.isEmpty()) {
                String url = toBeProcessedUrls.removeFirst();
                processingUrls.add(url);
                return url;
            }
            if (processingUrls.isEmpty()) {
                return null;
            } else {
                try {
                    wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    /**
     * Just a printing utility
     *
     * The method is not synchronized so the printed stats can present invalid values
     */
    private void printStats() {
        System.out.println("STATS - Thread: " + Thread.currentThread() + " Extracted links: " + links.size() + " Processed urls: " + processedUrls.size() + " - Processing urls: " + processingUrls.size() + " - To be processed urls: " + toBeProcessedUrls.size());
    }
}