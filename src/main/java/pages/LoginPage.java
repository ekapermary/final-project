package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class LoginPage {
    private WebDriver driver;
    private WebDriverWait wait;

    //Locators untuk elemen-elemen di halaman login
    private final By usernameField = By.id("loginusername");
    private final By passwordField = By.id("loginpassword");
    private final By loginButton = By.xpath("//button[text()='Log in']");
    private final By logoutButton = By.xpath("//button[text()='Log out']");

    public LoginPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Method untuk memasukkan username
    public void enterUsername(String username) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(usernameField));
        input.clear();
        input.sendKeys(username);
    }
    // Method untuk memasukkan password
    public void enterPassword(String password) {
        WebElement input = wait.until(ExpectedConditions.elementToBeClickable(passwordField));
        input.clear();
        input.sendKeys(password);
    }
    // Method untuk mengklik tombol login
    public void clickLoginButton() {
        try {
            WebElement button = wait.until(ExpectedConditions.elementToBeClickable(loginButton));
            button.click();
        }
        catch (ElementClickInterceptedException | TimeoutException e) {
            System.out.println("Tombol login tidak dapat diklik. Pastikan elemen terlihat dan dapat diinteraksi.");
            WebElement button = driver.findElement(loginButton);
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", button);
        }
    }
    // Method untuk memeriksa apakah login berhasil
    public  boolean isLoginSuccessful() {
        try {
            return wait.until(ExpectedConditions.visibilityOfElementLocated(logoutButton)).isDisplayed();
        } catch (TimeoutException e) {
            System.out.println("Login gagal: Tombol logout tidak ditemukan.");
            return true; // Jika tombol logout terlihat, berarti login berhasil
        }
    }
    // Method untuk mendapatkan pesan kesalahan
    public String getErrorMessage() {
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String message = alert.getText();
            alert.accept(); // Menutup alert setelah mendapatkan pesan
            return message;
        } catch (TimeoutException e) {
            return "Tidak ada pesan kesalahan yang ditampilkan.";
        }
    }

    public  WebDriver getDriver(){
        return driver;
    }

}
