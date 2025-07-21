package step_definitions;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import pages.SignupPage;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class SignupStep {
    private WebDriver driver;
    private SignupPage signupPage;
    private String uniqueUsername;

    //gnerate unique username
    private String generateUniqueUsername() {
        return "user" + UUID.randomUUID().toString().substring(0, 8);
    }

    public SignupStep() {
        this.driver = step_definitions.Hooks.getDriver();
        this.signupPage = new SignupPage(driver);
    }

    @When("user klik tombol sign up")
    public void userKlikTombolSignUp() {
        signupPage.openSignUpForm();
        uniqueUsername = generateUniqueUsername();
    }

    @And("user memasukkan username {string}")
    public void userMemasukkanUsername(String username) {
        if (username.equalsIgnoreCase("ekapermary")) {
            username = uniqueUsername;
        }
        signupPage.enterUsername(username);
    }

    @And("user memasukkan password {string}")
    public void userMemasukkanPassword(String password) {
        signupPage.enterPassword(password);
    }

    @And("user click tombol sign up")
    public void userClikTombolSignUp() {
        signupPage.clickSignUpButton();
    }

    @Then("sistem akan menampilkan pesan {string}")
    public void sistemAkanMenampilkanPesan(String expectedMessage) {
        String actualMessage = signupPage.getAlertMessage();
        assertEquals("Pesan tidak sesuai. Diharapkan: \"" + expectedMessage + "\", tapi muncul: \"" + actualMessage + "\"",
                expectedMessage, actualMessage);
    }
}
