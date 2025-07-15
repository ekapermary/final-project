package step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.WebDriver;
import pages.LoginPage;

public class LoginStep {
    private WebDriver driver;
    private CommonStep commonStep;
    private LoginPage loginPage;

    public LoginStep() {
        this.driver = Hooks.getDriver();
        this.commonStep = new CommonStep();
        this.loginPage = new LoginPage(driver);
    }

    @When("user klik Log in")
    public void userKlikLogIn() {
        commonStep.userMembukaHalamanUtamaDemoblaze();
        loginPage = new LoginPage(driver);
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
        loginPage.isLoginSuccessful() && driver.getCurrentUrl().contains("https://www.demoblaze.com/index.html"));
    }

}
