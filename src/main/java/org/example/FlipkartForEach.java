package org.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;

public class FlipkartForEach {
    public static void main(String[] args) {
        String url = "https://www.flipkart.com/";

        try {
            getLinks(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getLinks(String url) throws IOException {
        Document document = Jsoup.connect(url).get();

        document.select("a[href]")
                .forEach(FlipkartForEach::processLink);
    }

    private static void processLink(Element link) {
        String href = link.attr("abs:href");
        System.out.println(href);
    }
}

