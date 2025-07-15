package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class CartPage {
    private WebDriver driver;

    //constructor
    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    //locators untuk tombol checkout
    private By placeOrderButton = By.xpath("//button[text()='Place Order']");

    //method untuk mengklik tombol checkout
    public  void clickCheckoutButton() {
        WebElement checkoutButton = driver.findElement(placeOrderButton);
        checkoutButton.click();
    }
}
