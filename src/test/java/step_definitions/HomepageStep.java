package step_definitions;

import io.cucumber.java.en.Given;
import org.openqa.selenium.WebDriver;

public class HomepageStep {
    protected WebDriver driver;
    private SignupStep signupStep;

    //before
    public HomepageStep() {
        driver = Hooks.getDriver();
    }

    @Given ("user membuka halaman Demoblaze")
    public  void userMembukaHalamanDemoblaze() {
        driver.get("https://www.demoblaze.com/");
    }

    //after
    public  void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public void openURL(String url) {

    }
}
