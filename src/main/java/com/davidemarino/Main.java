package com.davidemarino;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Main class
 */
public class Main {

    /**
     * The main method. It takes 3 parameters
     * @param args must be of size 3. THe first parameter is the starting page, the second is the number of threads and
     *             the third is the maximum number of links to extract before stopping working threads
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            showUsage();
        }
        try {
            List<Link> links = Crawler.downloadLinks(args[0], Integer.parseInt(args[1]), Integer.parseInt(args[2]));

            System.out.println(
                    links.stream()
                            .sorted(Comparator.comparing(Link::getLabel))
                            .map(link -> "Page:  " + link.getPage() + "\nLabel: " + link.getLabel() + "\nLink:  " + link.getUrl() + "\n")
                            .collect(Collectors.joining("=========================================\n")));
        } catch (NumberFormatException e) {
            showUsage();
        }
    }

    private static void showUsage() {
        System.out.println("Usage: java -jar crawler.jar <url> [<number of threads>] [<maximum number of links to be extracted>]");
        System.exit(1);
    }
}