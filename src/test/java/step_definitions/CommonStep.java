package step_definitions;

import io.cucumber.java.en.Given;
import org.openqa.selenium.WebDriver;

public class CommonStep {

    private WebDriver driver;
    private HomepageStep homepageStep;

    @Given("user membuka halaman utama Demoblaze")
    public void userMembukaHalamanUtamaDemoblaze() {
        driver = Hooks.getDriver();
        homepageStep = new HomepageStep();
        homepageStep.openURL("https://www.demoblaze.com/");
    }
}
