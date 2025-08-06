package step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.CheckoutPage;
import pages.HomePage;
import pages.LoginPage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.Duration;

import static org.openqa.selenium.devtools.v137.page.Page.captureScreenshot;


public class LoginStep {
    private WebDriver driver;
    private CommonStep commonStep;
    private LoginPage loginPage;
    private HomePage homePage;
    private org.openqa.selenium.support.ui.WebDriverWait wait;
    private static final int DEFAULT_TIMEOUT = 30;


    public LoginStep() {
        this.driver = Hooks.getDriver();
//        this.commonStep = new CommonStep();
        this.homePage = new HomePage(driver);
        this.loginPage = new LoginPage(driver);
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(DEFAULT_TIMEOUT));
    }


    @When("user klik Log in")
    public void userKlikLogIn() {
        //commonStep.userMembukaHalamanUtamaDemoblaze();
        loginPage = new LoginPage(driver);
        loginPage.clikLoginLink();
    }

    @And("user input {string} sebagai username")
    public void userInputSebagaiUsername(String username) {
        loginPage.enterUsername(username);
    }

    @And("user input {string} sebagai password")
    public void userInputSebagaiPassword(String password) {
        loginPage.enterPassword(password);
    }

    @And("user klik tombol Log in")
    public void userKlikTombolLogIn() {
        loginPage.clickLoginButton();
    }

    @Then("user akan diarahkan ke halaman utama")
    public void userAkanDiarahkanKeHalamanUtama() {
        Assert.assertTrue("login gagal, halaman utama tidak ditampilkan",
                loginPage.isLoginSuccessful());
    }

    @Then("sistem menampilkan pesan {string}")
    public void verifyLoginErrorMessage(String expectedMessage) {
        try {
            Alert alert = wait.until(ExpectedConditions.alertIsPresent());
            String actualMessage = alert.getText();
            Assertions.assertTrue(actualMessage.contains(expectedMessage),
                    "Pesan error tidak sesuai. Diharapkan: " + expectedMessage + ", tetapi didapat: " + actualMessage);
            alert.accept();
        } catch (TimeoutException e) {
            File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            try {
                Files.copy(screenshot.toPath(), Paths.get("screenshots", "login_error.png"));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            Assertions.fail("Tidak ada alert yang muncul dalam waktu yang ditentukan");
        }
    }

}