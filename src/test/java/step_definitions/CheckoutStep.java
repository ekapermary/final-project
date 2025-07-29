package step_definitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.After;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.CheckoutData;
import pages.CheckoutPage;
import pages.HomePage;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;

public class CheckoutStep {

    private WebDriver driver;
    private WebDriverWait wait;
    private HomePage homePage;
    private CheckoutPage checkoutPage;
    private static final int DEFAULT_TIMEOUT = 30;


    //@Given("user membuka halaman Demoblaze")
    public void userMembukaHalamanDemoblaze() {
        setupDriver();
        homePage.openHomePage();
        waitForVisibility(By.xpath("//*[@id='navbarExample']/ul/li[1]/a"));
    }

    @When("user menambahkan produk ke keranjang")
    public void userMenambahkanProdukKeKeranjangDefault() {
        String defaultProduct = "Samsung galaxy s6";
        log("User menambahkan produk default: " + defaultProduct);
        homePage.selectProduct(defaultProduct);
        homePage.addToCart();
        handleAlertIfPresent();
    }

    @And("user memilih produk {string}")
    public void userMemilihProduk(String productName) {
        log("User memilih produk: " + productName);
        homePage.selectProduct(productName);
    }


//    @And("user menambahkan produk ke keranjang")
//    public void userMenambahkanProdukKeKeranjangSetelahPilih() {
//        homePage.addToCart();
//        handleAlertIfPresent();
//    }

    @And("user membuka halaman cart")
    public void goToCartPage() {
        homePage.goToCart();
    }

    @And("user membuka halaman checkout")
    public void openCheckoutForm() {
        checkoutPage.clickPlaceOrder();
        waitForVisibility(By.id("orderModalLabel"));
    }

    @And("user melengkapi form checkout dengan data berikut:")
    public void checkoutWithData(DataTable data) {
        fillCheckoutForm(data);
    }

    @And("user mengklik tombol Purchase")
    public void clickPurchase() {
        checkoutPage.clickPurchase();
    }

    @And("user mengklik tombol Purchase dengan data validitas")
    public void clickPurchaseBasedOnValidation() {
        if (isFormDataValid()) {
            checkoutPage.clickPurchase();
        } else {
            log("Form data tidak valid, tombol Purchase tidak diklik");
        }
    }

    @Then("sistem menampilkan pesan sukses dengan teks {string}")
    public void verifySuccessfullOrder(String expectedMessage) {
        verifySuccessMessage(expectedMessage);
    }

    @Then("user menutup notifikasi pop up")
    public void closeSuccessPopup() {
        try {
            waitForVisibility(By.xpath("//div[contains(@class, 'sweet-alert')]//h2[contains(text(), 'Thank you for your purchase!')]"), DEFAULT_TIMEOUT + 5);
            waitForClickable(By.xpath("//button[contains(@class, 'confirm') and text()='OK']")).click();
        } catch (Exception e) {
            captureScreenshot("popup_error.png");
            throw e;
        }
    }

    @Then("sistem menampilkan pesan {string} sesuai hasil {word}")
    public void verifyMessageBasedOnSuccess(String expectedMessage, String inSuccessStr) {
        boolean isSuccess = Boolean.parseBoolean(inSuccessStr);
        if (isSuccess) {
            verifySuccessMessage(expectedMessage);
        } else {
            verifyNoPopupOnInvalid(expectedMessage);
        }
    }

    @After
    public void tearDown() {
        if (driver != null) driver.quit();
        CheckoutData.clear();
    }

    // Helper methods
    private void setupDriver() {
        WebDriverManager.chromedriver().setup();
        driver = Hooks.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        homePage = new HomePage();
        checkoutPage = new CheckoutPage(driver);
    }

    private void fillCheckoutForm(DataTable data) {
        Map<String, String> formData = data.asMap(String.class, String.class);
        CheckoutData.setFormData(
                formData.get("name"),
                formData.get("country"),
                formData.get("city"),
                formData.get("card"),
                formData.get("month"),
                formData.get("year")
        );

        checkoutPage.enterCheckoutDetails(
                CheckoutData.getName(),
                CheckoutData.getCountry(),
                CheckoutData.getCity(),
                CheckoutData.getCard(),
                CheckoutData.getMonth(),
                CheckoutData.getYear()
        );
    }

    private boolean isFormDataValid() {
        return isFilled(CheckoutData.getName()) &&
                isFilled(CheckoutData.getCard());
    }

    private boolean isFilled(String value) {
        return value != null && !value.trim().isEmpty();
    }

    private void verifySuccessMessage(String expectedMessage) {
        try {
            WebElement successElement = waitForVisibility(By.xpath("//div[contains(@class, 'sweet-alert')]//h2"), DEFAULT_TIMEOUT + 5);
            String actualMessage = successElement.getText();
            Assertions.assertTrue(actualMessage.contains(expectedMessage), "Pesan tidak sesuai");
        } catch (TimeoutException e) {
            captureScreenshot("checkout_success_error.png");
            throw e;
        }
    }

    private void verifyNoPopupOnInvalid(String expectedMessage) {
        try {
            waitForVisibility(By.xpath("//div[contains(@class, 'sweet-alert')]//h2"), 5);
            captureScreenshot("popup_error.png");
            Assertions.fail("Popup muncul padahal data tidak valid");
        } catch (TimeoutException e) {
            log("Tidak ada popup yang muncul, sesuai harapan karena data tidak valid");
        }
    }

    private void handleAlertIfPresent() {
        try {
            WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            Alert alert = alertWait.until(ExpectedConditions.alertIsPresent());
            alert.accept();
        } catch (TimeoutException e) {
            log("Tidak ada alert yang muncul setelah klik add to cart");
        }
    }

    private WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private WebElement waitForVisibility(By locator, int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds)).until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private WebElement waitForClickable(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    private void captureScreenshot(String filename) {
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            File dir = new File("screenshots/");
            if (!dir.exists()) dir.mkdirs();
            FileUtils.copyFile(screenshot, new File(dir, filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void log(String message) {
        System.out.println(message);
    }
}
