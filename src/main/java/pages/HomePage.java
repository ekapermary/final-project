package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public class HomePage {

    private WebDriver driver;
    private WebDriverWait Wait;

    //locators
    private final By cartButton = By.id("cartur");
    private final By loginButton = By.id("login2");
    private final By welcomeMessage = By.id("nameofuser");
    private final By productList = By.cssSelector(".card-title a");
    private final By addToCartButton = By.xpath("//a[contains(text(),'Add to cart')]");
    private final By placeOrderButton = By.xpath("//button[@class='btn btn-success']");

    public  HomePage(WebDriver driver) {
        this.driver = driver;
        this.Wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    //navigasi

    public void openHomePage() {
        driver.get("https://www.demoblaze.com/");
    }

    public void openURL(String url) {
        driver.get(url);
    }

    //actions

    public void selectProduct(String productName) {
        Wait.until(ExpectedConditions.presenceOfElementLocated(productList));
        List<WebElement> products = driver.findElements(productList);

        for (WebElement product : products) {
            if (product.getText().equalsIgnoreCase(productName)) {
                clickWithFallback(product);
                return;
            }
        }

        throw new RuntimeException("Product not found: " + productName);
    }

    public void addToCart() {
        clickWithFallback(addToCartButton);
    }

    public void goToCart() {
        clickWithFallback(cartButton);
    }

    public  void clickLoginButton() {
        clickWithFallback(loginButton);
    }

    public String getWelcomeMessage() {
        return Wait.until(ExpectedConditions.visibilityOfElementLocated(welcomeMessage)).getText();
    }

    public boolean isUserLoggedin() {
        return Wait.until(ExpectedConditions.visibilityOfElementLocated(welcomeMessage)).isDisplayed();
    }

    //checkout
    public void proceedToCheckout() {
        clickWithFallback(placeOrderButton);
    }

    //helper methods
    private void clickWithFallback(By locator) {
        try {
            WebElement element = Wait.until(ExpectedConditions.elementToBeClickable(locator));
            scrollToElement(element);
            element.click();
        } catch (TimeoutException | ElementClickInterceptedException e) {
            System.out.println("Element not clickable, trying JavaScript click");
            WebElement element = driver.findElement(locator);
            jsClick(element);
        }
    }

    private void clickWithFallback(WebElement element) {
        try {
            Wait.until(ExpectedConditions.elementToBeClickable(element));
            scrollToElement(element);
            element.click();
        } catch (Exception e) {
            System.out.println("Element not clickable, trying JavaScript click");
            jsClick(element);
        }
    }

    private void scrollToElement(WebElement element){
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    private void jsClick(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

}
