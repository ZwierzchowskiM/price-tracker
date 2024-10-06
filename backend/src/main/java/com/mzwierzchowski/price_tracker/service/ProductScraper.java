package com.mzwierzchowski.price_tracker.service;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ProductScraper {


  public static WebDriver getDriver() {
    WebDriverManager.chromedriver().setup();
    ChromeOptions options = new ChromeOptions();
    options.addArguments("--headless=new");
    options.addArguments("--disable-gpu");
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage"); // Poprawa działania w środowiskach o ograniczonej pamięci (np. Docker)


    return new ChromeDriver(options);
  }

  public static String getProductNameFromUrl(String url) {
    WebDriver driver = getDriver();

    try {
      driver.get(url);

      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
      JavascriptExecutor js = (JavascriptExecutor) driver;
      wait.until(webDriver -> js.executeScript("return document.readyState").equals("complete"));

      List<WebElement> cookiesButtons = driver.findElements(By.cssSelector("button[data-testid='cookie-banner-accept-all']"));
      if (!cookiesButtons.isEmpty()) {
        WebElement acceptCookiesButton = cookiesButtons.getFirst();
        if (acceptCookiesButton.isDisplayed() && acceptCookiesButton.isEnabled()) {
          acceptCookiesButton.click();
        }
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
                      + "    child = child.nextSibling;"
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
    WebDriver driver = getDriver();

    try {
      driver.get(url);

      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
      JavascriptExecutor js = (JavascriptExecutor) driver;
      wait.until(webDriver -> js.executeScript("return document.readyState").equals("complete"));

      List<WebElement> cookiesButtons = driver.findElements(By.cssSelector("button[data-testid='cookie-banner-accept-all']"));
      if (!cookiesButtons.isEmpty()) {
        WebElement acceptCookiesButton = cookiesButtons.get(0);
        if (acceptCookiesButton.isDisplayed() && acceptCookiesButton.isEnabled()) {
          acceptCookiesButton.click();
        }
      }

      WebElement priceSection =
          driver.findElement(By.cssSelector("section.wrapperstyled__PriceWrapper-xngval-3"));

      WebElement priceElement;
      List<WebElement> discountPriceElements = priceSection.findElements(By.cssSelector("[data-selen='product-discount-price']"));
      if (!discountPriceElements.isEmpty()) {
        priceElement = discountPriceElements.getFirst();
      } else {
        priceElement = priceSection.findElement(By.cssSelector("[data-selen='product-price']"));  // Jeśli nie znaleziono ceny zniżkowej, pobierz standardową cenę
      }

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
