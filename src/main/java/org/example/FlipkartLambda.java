package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.List;


import java.util.stream.Collectors;

public class FlipkartLambda{
    public static void main(String[] args) {
        String url = "https://www.flipkart.com/";

        try {
            List<String> links = getLinks(url);
            links.forEach(System.out::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<String> getLinks(String url) throws IOException {
        Document document = Jsoup.connect(url).get();

        return document.select("a[href]")
                .stream()
                .map(link -> link.attr("abs:href"))
                .collect(Collectors.toList()); // Use this instead of toList()
    }
}


