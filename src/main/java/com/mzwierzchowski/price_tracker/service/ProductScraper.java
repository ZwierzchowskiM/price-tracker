package com.mzwierzchowski.price_tracker.service;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductScraper {


  public static String getProductNameFromUrl(String url) {
    System.setProperty(
        "webdriver.chrome.driver",
        "C:\\Users\\marci\\OneDrive\\Pulpit\\chromedriver-win32\\chromedriver.exe");

    ChromeOptions options = new ChromeOptions();
    options.addArguments(
        "--headless"); // Usuń tę linię, jeśli chcesz obserwować działanie przeglądarki
    options.addArguments("--disable-gpu");
    options.addArguments("--no-sandbox");
    options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
    options.addArguments("--disable-blink-features=AutomationControlled");

    WebDriver driver = new ChromeDriver(options);

    try {
      driver.get(url);

      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
      JavascriptExecutor js = (JavascriptExecutor) driver;
      wait.until(webDriver -> js.executeScript("return document.readyState").equals("complete"));

      try {
        WebElement acceptCookiesButton =
            wait.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button[data-testid='cookie-banner-accept-all']")));
        acceptCookiesButton.click();
      } catch (Exception e) {
      }

      WebElement productNameElement =
          driver.findElement(By.cssSelector("h1[data-testid='product-name']"));

      System.out.println("HTML elementu: " + productNameElement.getAttribute("outerHTML"));

      String productNameText =
          (String)
              js.executeScript(
                  "var element = arguments[0];"
                      + "var child = element.firstChild;"
                      + "while(child && child.nodeType != 3) {"
                      +
                      "    child = child.nextSibling;"
                      + "}"
                      + "return child ? child.nodeValue.trim() : '';",
                  productNameElement);

      System.out.println("Pobrana nazwa produktu: " + productNameText);

      return productNameText;

    } catch (TimeoutException e) {
      System.err.println(
          "TimeoutException: Nie udało się znaleźć elementu w podanym czasie dla URL: " + url);
      e.printStackTrace();
      return null;
    } catch (Exception e) {
      System.err.println("Błąd podczas pobierania nazwy produktu dla URL: " + url);
      e.printStackTrace();
      return null;
    } finally {
      driver.quit();
    }
  }

  public static Double getProductPriceFromUrl(String url) {
    System.setProperty(
        "webdriver.chrome.driver",
        "C:\\Users\\marci\\OneDrive\\Pulpit\\chromedriver-win32\\chromedriver.exe");

    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless");
    options.addArguments("--disable-gpu");
    options.addArguments("--no-sandbox");
    options.addArguments("user-agent=Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
    options.addArguments("--disable-blink-features=AutomationControlled");

    WebDriver driver = new ChromeDriver(options);

    try {
      driver.get(url);
      try {
        WebDriverWait waitPopup = new WebDriverWait(driver, Duration.ofSeconds(10));
        WebElement acceptCookiesButton =
            waitPopup.until(
                ExpectedConditions.elementToBeClickable(
                    By.cssSelector("button[data-testid='cookie-banner-accept-all']")));
        acceptCookiesButton.click();
      } catch (Exception e) {
        //
      }

      WebElement priceSection =
          driver.findElement(By.cssSelector("section.wrapperstyled__PriceWrapper-xngval-3"));

      WebElement priceElement = null;
      try {
        priceElement =
            priceSection.findElement(By.cssSelector("[data-selen='product-discount-price']"));
      } catch (Exception e) {
        priceElement = priceSection.findElement(By.cssSelector("[data-selen='product-price']"));
      }

      JavascriptExecutor js = (JavascriptExecutor) driver;
      String priceText =
          (String)
              js.executeScript(
                  "var element = arguments[0];"
                      + "var child = element.firstChild;"
                      + "while(child && child.nodeType != 3) {"
                      + // 3 oznacza węzeł tekstowy
                      "    child = child.nextSibling;"
                      + "}"
                      + "return child ? child.nodeValue.trim() : '';",
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
