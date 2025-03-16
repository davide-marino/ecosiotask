package com.davidemarino;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Utility class with a method to download a page and convert the body to a String
 */
public class Downloader {

    /**
     * This is an utility class that can be used only with static methods
     */
    private Downloader() {

    }

    /**
     * This method returns the headers to be set for the http calls
     *
     * @return the array of headers
     */
    private static String[] getHeaders() {
        return new String[]{ "User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/134.0.0.0 Safari/537.36" };
    }

    /**
     * Returns the body of a page as String
     *
     * @param url the url of the page to be downloaded
     * @return The body of the page as String
     */
    public static String downloadContent(String url) {
        try {
            HttpClient client = HttpClient.newBuilder().followRedirects(HttpClient.Redirect.ALWAYS).build();

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .headers(getHeaders())
                    .GET()
                    .build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}