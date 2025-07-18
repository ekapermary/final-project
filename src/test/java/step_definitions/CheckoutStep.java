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

import static io.opentelemetry.api.internal.ApiUsageLogger.log;
import static org.openqa.selenium.devtools.v135.page.Page.captureScreenshot;

public class CheckoutStep {

    private WebDriver driver;
    private WebDriverWait wait;
    private HomePage homePage;
    private CheckoutPage checkoutPage;
    private static final int DEFAULT_TIMEOUT = 30;

    //step definitions

    @Given("user membuka halaman Demoblaze")
    public void userMembukaHalamanDemoblaze() {
        setupDriver();
        homePage.openHomePage();
        waitForVisibility(By.xpath("//*[@id='navbarExample']/ul/li[1]/a"));
    }

    @When("user menambahkan produk {string} ke keranjang")
    public void addProductToCart(String productName) {
        log ("Add product ke keranjang: " + productName);
        homePage.selectProduct(productName);
        homePage.addToCart();
        handleAlertIfPresent();
    }

    @And("user membuka halaman cart")
    public void goToCartPage(){
        homePage.goToCart();
    }

    @When("user membuka form checkout")
    public void openCheckoutForm(){
        checkoutPage.clickPlaceOrder();
        waitForVisibility(By.id("orderModalLabel"));
    }

    @When("user melengkapi form checkout dengan data berikut:")
    public void checkoutWithData (DataTable data){
        fillCheckoutForm(data);
    }

    @When ("user klik tombol Purchase")
    public void clickPurchaseBasedOnValidation() {
        log("Menentukan apakah perlu Klik tombol Purchase berdasarkan validasi");
        if (isFormDataValid()) {
            log("Form data valid, melakukan klik Purchase");
            checkoutPage.clickPurchase();
        } else {
            log("Form data tidak valid, tidak melakukan klik Purchase");
        }
    }

    //Checkout steps mengubah verifyMessageBasedOnSuccess dan antosinya

    @Then("Akan menampilkan {string} sesuai hasil {word}")
    public void verifyMessageBasedOnSuccess(String expectedMessage, String inSuccessStr) {
        boolean isSuccess = Boolean.parseBoolean(inSuccessStr);
        if (isSuccess) {
            verifySuccessMessage(expectedMessage);
        } else {
            verifyNoPopupOnInvalid(expectedMessage);
        }
    }


    @Then("Order berhasil di proses dan menampilkan pesan {string}")
    public void verifySuccessfullOrder(String expectedMessage){
        verifySuccessMessage(expectedMessage);
    }

    @Then("user menutup pop up konfirmasi pmbelian")
    public void cloeseSuccessPopup() {
        try {
            waitForVisibility(By.xpath("//div[contains(@class, 'sweet-alert')]//h2[contains(text(), 'Thank you for your purchase!')]"), DEFAULT_TIMEOUT + 5);
            waitForClickable(By.xpath("//button[contains(@class, 'confirm') and text()='OK']")).click();
            log("Tombol Ok pada pop up konfirmasi pembelian berhasil diklik");
        } catch (Exception e) {
            log("Gagal menutup pop up konfirmasi pembelian: " + e.getMessage());
            captureScreenshot("popup_error.png");
            throw e;
        }
    }

    
    @After
    public void tearDown() {
        if (driver != null)
            driver.quit();
            CheckoutData.clear();

    }

    // Helper methods
    private void setupDriver(){
        WebDriverManager.chromedriver().setup();
        driver = Hooks.getDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
        homePage = new HomePage();
        checkoutPage = new CheckoutPage(driver);
    }

    private void fillCheckoutForm(DataTable data){
        Map<String, String> formData = data.asMap(String.class, String.class);
        CheckoutData.setFormData(
                formData.get("name"),
                formData.get("country"),
                formData.get("city"),
                formData.get("card"),
                formData.get("month"),
                formData.get("year")
                );

        log("Mengisi form checkout dengan data: " + formData);
        logCheckoutData();

        checkoutPage.enterCheckoutDetails(
                CheckoutData.getName(),
                CheckoutData.getCountry(),
                CheckoutData.getCity(),
                CheckoutData.getCard(),
                CheckoutData.getMonth(),
                CheckoutData.getYear()
        );
    }

    private boolean isFormDataValid(){
        return isFilled(CheckoutData.getName()) &&
               isFilled(CheckoutData.getCard());
    }

    private boolean isFilled(String value){
        return value != null && !value.trim().isEmpty();
    }

    private void verifySuccessMessage(String expectedMessage){
        try {
            WebElement successElement = waitForVisibility(By.xpath("//div[contains(@class, 'sweet-alert')]//h2"), DEFAULT_TIMEOUT + 5);
            String actualMessage = successElement.getText();
            log("Pesan Success muncul: " + actualMessage);
            Assertions.assertTrue(actualMessage.contains(expectedMessage), "Pesan tidak sesuai");
        } catch (TimeoutException e){
            log("Gagal menampilkan pesan Success");
            captureScreenshot("checkout_success_error.png");
            throw e; }
        }

    private void verifyNoPopupOnInvalid(String expectedMessage)  {
        try {
            waitForVisibility(By.xpath("//div[contains(@class, 'sweet-alert')]//h2"), 5);
            captureScreenshot("popup_error.png");
            Assertions.fail("Popup muncul padahal data tidak valid");
        } catch (TimeoutException e) {
            log("Tidak ada popup yang muncul, sesuai harapan karena data tidak valid");
        }
    }

    private void handleAlertIfPresent(){
        try{
            WebDriverWait alertWait = new WebDriverWait(driver, Duration.ofSeconds(5));
            Alert alert = alertWait.until(ExpectedConditions.alertIsPresent());
            log("Alert muncul: " + alert.getText());
            alert.accept();
            log("Close alert");
        } catch (TimeoutException e) {
            log("Tidak ada alert yang muncul setelah klik add to cart"); }
        }

    private WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    private WebElement waitForVisibility(By locator, int timeoutSeconds) {
        return new WebDriverWait(driver, Duration.ofSeconds(timeoutSeconds))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
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
            log("Screenshoot disimpan di : screeenshots/" + filename);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    private void logCheckoutData(){
        log("    - Nama   : " + CheckoutData.getName());
        log("    - Negara : " + CheckoutData.getCountry());
        log("    - Kota   : " + CheckoutData.getCity());
        log("    - Kartu  : " + CheckoutData.getCard());
        log("    - Bulan  : " + CheckoutData.getMonth());
        log("    - Tahun  : " + CheckoutData.getYear());
    }

    private void log(String message) {
        System.out.println(message);
    }

}
