package com.davidemarino;

/**
 * Immutable class that represents a Link with a page, url and label
 */
public final class Link {
    private final String page;
    private final String url;
    private final String label;

    public Link(String page, String url, String label) {
        this.page = page;
        this.url = url;
        this.label = label;
    }

    public String getUrl() {
        return url;
    }

    public String getLabel() {
        return label;
    }

    public String getPage() {
        return page;
    }
}