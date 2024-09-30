package com.mzwierzchowski.price_tracker.service;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PriceScraper {
    public static Double getPriceFromUrl(String url) {
    System.setProperty(
        "webdriver.chrome.driver",
        "C:\\Users\\marci\\OneDrive\\Pulpit\\chromedriver-win32\\chromedriver.exe");

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");

        WebDriver driver = new ChromeDriver(options);

        try {
            driver.get(url);

//            // Obsługa popupów
//            try {
//                WebDriverWait waitPopup = new WebDriverWait(driver, Duration.ofSeconds(5));
//                WebElement acceptCookiesButton = waitPopup.until(ExpectedConditions.elementToBeClickable(
//                        By.cssSelector("button[data-testid='cookie-banner-accept-all']")));
//                acceptCookiesButton.click();
//            } catch (Exception e) {
//                // Brak popupu
//            }
//
//            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(30));
//
//            // Zaktualizowany selektor
//            WebElement priceElement = wait.until(ExpectedConditions.visibilityOfElementLocated(
//                    By.cssSelector("div.basic-price.promo-price, div.basic-price")));

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));

            WebElement priceElement = null;
            try {
                priceElement = driver.findElement(By.cssSelector("[data-selen='product-discount-price']"));
            } catch (Exception e) {
                priceElement = driver.findElement(By.cssSelector("[data-selen='product-price']"));
            }
            System.out.println("HTML elementu: " + priceElement.getAttribute("outerHTML"));

            JavascriptExecutor js = (JavascriptExecutor) driver;
            String priceText = (String) js.executeScript(
                    "var element = arguments[0];" +
                            "var child = element.firstChild;" +
                            "while(child && child.nodeType != 3) {" + // 3 oznacza węzeł tekstowy
                            "    child = child.nextSibling;" +
                            "}" +
                            "return child ? child.nodeValue.trim() : '';",
                    priceElement);

            System.out.println("Pobrany tekst ceny: " + priceText);

            if (priceText == null || priceText.isEmpty()) {
                priceText = priceElement.getText().trim();
                System.out.println("Alternatywny tekst ceny: " + priceText);
            }

            if (priceText != null && !priceText.isEmpty()) {
                Pattern pattern = Pattern.compile("([0-9]+[.,][0-9]+)");
                Matcher matcher = pattern.matcher(priceText);
                if (matcher.find()) {
                    String priceNumber = matcher.group(1).replace(",", ".").trim();
                    return Double.parseDouble(priceNumber);
                } else {
                    System.err.println("Nie udało się wyodrębnić liczby z tekstu ceny dla URL: " + url);
                    return null;
                }
            } else {
                System.err.println("Nie udało się pobrać tekstu ceny dla URL: " + url);
                return null;
            }
        } catch (Exception e) {
            System.err.println("Błąd podczas pobierania ceny dla URL: " + url);
            e.printStackTrace();
            return null;
        } finally {
            driver.quit();
        }
    }

}