package com.davidemarino;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Utility class to extract the links from a string representing the body of an html page
 */
public class LinksExtractor {
    private static final String ANCHOR_REGEX = "<a\\s+[^>]*href=[\"']([^\"']*)[\"'][^>]*>(.*?)</a>";
    private static final Pattern ANCHOR_PATTERN = Pattern.compile(ANCHOR_REGEX);
    public static final String TAGS_REGEX = "<[^>]*>";

    /**
     * This is an utility class that can be used only with static methods
     */
    private LinksExtractor() {
    }

    /**
     * Given a page content extract all urls of that page and returns them as a List<Link>
     *
     * @param basePath the URI of the starting page
     * @param pageUrl the url of the page
     * @param content the content of the page
     * @return the list of Link present on the page referring the same context (website)
     */
    public static List<Link> extractLinks(URI basePath, String pageUrl, String content) {
        Matcher matcher = ANCHOR_PATTERN.matcher(content);
        List<Link> links = new ArrayList<>();
        while (matcher.find()) {
            String url = getAbsoluteUrl(basePath, pageUrl, matcher.group(1));
            String label = matcher.group(2).replaceAll(TAGS_REGEX, "");
            if (url != null) {
                links.add(new Link(pageUrl, url, label));
            }
        }
        return links;
    }

    /**
     * Given a page url and a url returns the absolute url. If the url is relative it returns the absolute url
     * built starting from the pageUrl.
     * If the url is absolute it returns null if the absolute url is not related to the same context (website)
     * so if scheme, host, port are not equals.
     *
     * @param pageUrl The absolute url of the page containing the url
     * @param url The url to return as absolute
     * @return the url as absolute if referencing the same context or null otherwise
     */
    private static String getAbsoluteUrl(URI basePath, String pageUrl, String url) {
        try {
            URI baseUri = new URI(pageUrl);
            URI resolvedUri = baseUri.resolve(url);
            if (resolvedUri != null && resolvedUri.getPath() != null
                    && resolvedUri.getScheme().equals(basePath.getScheme())
                    && resolvedUri.getHost().equals(basePath.getHost())
                    && resolvedUri.getPort() == basePath.getPort()
            ) {
                return resolvedUri.toString();
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }
}