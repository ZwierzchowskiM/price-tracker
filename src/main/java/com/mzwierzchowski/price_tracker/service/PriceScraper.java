package com.mzwierzchowski.price_tracker.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class PriceScraper {
    public static Double getPriceFromUrl(String url) {
        try {
            Document doc = Jsoup.connect(url)
                    .userAgent("Mozilla/5.0")
                    .timeout(10000)
                    .get();

            String priceText = null;

            Elements promoPriceElements = doc.select("[data-selen='product-discount-price']");
            if (!promoPriceElements.isEmpty()) {
                priceText = promoPriceElements.first().text();
            } else {
                Elements regularPriceElements = doc.select("[data-selen='product-price']");
                if (!regularPriceElements.isEmpty()) {
                    priceText = regularPriceElements.first().text();
                } else {
                    System.err.println("Cena nie została znaleziona dla URL: " + url);
                    return null;
                }
            }

            if (priceText != null) {
                priceText = priceText.replaceAll("[^0-9,]", "").replace(",", ".").trim();
                return Double.parseDouble(priceText);
            }

        } catch (IOException e) {
            System.err.println("Błąd podczas łączenia z URL: " + url);
            e.printStackTrace();
        } catch (NumberFormatException e) {
            System.err.println("Błąd konwersji ceny na liczbę dla URL: " + url);
            e.printStackTrace();
        }

        return null;
    }
}