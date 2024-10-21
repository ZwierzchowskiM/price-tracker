package com.mzwierzchowski.price_tracker.service;

import io.github.bonigarcia.wdm.WebDriverManager;
import java.time.Duration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.extern.log4j.Log4j2;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

@Log4j2
public class ProductScraper {

  public static WebDriver getDriver() {
    WebDriverManager.chromedriver().setup();
    ChromeOptions options = new ChromeOptions();
    options.setExperimentalOption("detach", false);
    options.addArguments("--headless=old");
    options.addArguments("--disable-gpu");
    options.addArguments("--no-sandbox");
    options.addArguments("--disable-dev-shm-usage");
    options.addArguments("--disable-extensions");
    options.addArguments("--disable-software-rasterizer");
    options.addArguments("--disable-background-networking");

    return new ChromeDriver(options);
  }

  public static String getProductNameFromUrl(String url) {
    WebDriver driver = getDriver();

    try {
      log.info("Scraping product name from URL: {}", url);
      driver.get(url);

      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
      JavascriptExecutor js = (JavascriptExecutor) driver;
      wait.until(webDriver -> js.executeScript("return document.readyState").equals("complete"));

      List<WebElement> cookiesButtons =
          driver.findElements(By.cssSelector("button[data-testid='cookie-banner-accept-all']"));
      if (!cookiesButtons.isEmpty()) {
        WebElement acceptCookiesButton = cookiesButtons.get(0);
        if (acceptCookiesButton.isDisplayed() && acceptCookiesButton.isEnabled()) {
          acceptCookiesButton.click();
        }
      }

      WebElement productNameElement =
          driver.findElement(By.cssSelector("h1[data-testid='product-name']"));

      log.debug(
          "HTML of the product name element: {}", productNameElement.getAttribute("outerHTML"));

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

      log.info("Extracted product name: {}", productNameText);

      return productNameText;

    } catch (TimeoutException e) {
      log.error(
          "TimeoutException: Failed to find the element within the allotted time for URL: {}",
          url,
          e);
      return null;
    } catch (Exception e) {
      log.error("Error while extracting the product name for URL: {}", url, e);
      return null;
    } finally {
      driver.quit();
    }
  }

  public static Double getProductPriceFromUrl(String url) {
    WebDriver driver = getDriver();

    try {
      log.info("Scraping product price from URL: {}", url);
      driver.get(url);

      WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(3));
      JavascriptExecutor js = (JavascriptExecutor) driver;
      wait.until(webDriver -> js.executeScript("return document.readyState").equals("complete"));

      List<WebElement> cookiesButtons =
          driver.findElements(By.cssSelector("button[data-testid='cookie-banner-accept-all']"));
      if (!cookiesButtons.isEmpty()) {
        WebElement acceptCookiesButton = cookiesButtons.get(0);
        if (acceptCookiesButton.isDisplayed() && acceptCookiesButton.isEnabled()) {
          acceptCookiesButton.click();
        }
      }

      WebElement priceSection =
              driver.findElement(By.cssSelector("section[class*='wrapperstyled__PriceWrapper']"));

      WebElement priceElement;
      List<WebElement> discountPriceElements =
          priceSection.findElements(By.cssSelector("[data-selen='product-discount-price']"));
      if (!discountPriceElements.isEmpty()) {
        priceElement = discountPriceElements.get(0);
      } else {
        priceElement = priceSection.findElement(By.cssSelector("[data-selen='product-price']"));
      }

      String priceText =
          (String)
              js.executeScript(
                  "var element = arguments[0];"
                      + "var child = element.firstChild;"
                      + "while(child && child.nodeType != 3) {"
                      + "    child = child.nextSibling;"
                      + "}"
                      + "return child ? child.nodeValue.trim() : '';",
                  priceElement);

      log.debug("Extracted price text: {}", priceText);

      if (priceText == null || priceText.isEmpty()) {
        priceText = priceElement.getText().trim();
        log.debug("Alternative price text: {}", priceText);
      }

      if (priceText != null && !priceText.isEmpty()) {
        Pattern pattern = Pattern.compile("([0-9]+[.,][0-9]+)");
        Matcher matcher = pattern.matcher(priceText);
        if (matcher.find()) {
          String priceNumber = matcher.group(1).replace(",", ".").trim();
          Double price = Double.parseDouble(priceNumber);
          log.info("Extracted product price: {}", price);
          return price;
        } else {
          log.error("Failed to extract numeric value from the price text for URL: {}", url);
          return null;
        }
      } else {
        log.error("Failed to retrieve price text for URL: {}", url);
        return null;
      }
    } catch (Exception e) {
      log.error("Error while extracting the product price for URL: {}", url, e);
      return null;
    } finally {
      driver.quit();
    }
  }
}
