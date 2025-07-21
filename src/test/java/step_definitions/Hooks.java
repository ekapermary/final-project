package step_definitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class Hooks {
    // Gunakan ThreadLocal untuk mendukung parallel execution
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    // Sebelum setiap scenario
    @Before
    public void setUp() {
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("--headless"); // Untuk CI/CD
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");

        WebDriverManager.chromedriver().setup();
        WebDriver driver = new ChromeDriver(options);
        driver.manage().window().maximize();

        driverThreadLocal.set(driver);
        log("WebDriver initialized.");
    }

    // Setelah setiap scenario
    @After
    public void tearDown(Scenario scenario) {
        WebDriver driver = getDriver();

        if (scenario.isFailed()) {
            attachScreenshot(scenario, driver);
        }

        if (driver != null) {
            driver.quit();
            log("WebDriver quit.");
        }
        driverThreadLocal.remove();
    }

    // Ambil screenshot saat gagal
    private void attachScreenshot(Scenario scenario, WebDriver driver) {
        try {
            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenshot, "image/png", "Failure Screenshot");
            log("Screenshot attached to failed scenario.");
        } catch (WebDriverException e) {
            log("Failed to take screenshot: " + e.getMessage());
        }
    }

    // Public method untuk mendapatkan driver yang aman
    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();

        try {
            if (!isSessionActive(driver)) {
                log("WebDriver session invalid or null. Reinitializing...");
                new Hooks().setUp(); // panggil setUp manual
                driver = driverThreadLocal.get();
            }
        } catch (Exception e) {
            log("Error in getDriver(): " + e.getMessage());
            new Hooks().setUp(); // fallback jika error lain
            driver = driverThreadLocal.get();
        }

        return driver;
    }

    // Cek apakah session masih aktif
    private static boolean isSessionActive(WebDriver driver) {
        try {
            if (driver == null) return false;
            driver.getTitle(); // harmless check
            return true;
        }
        catch (NoSuchSessionException e) {
            return false;
        }
        catch (WebDriverException e) {
            return false;
        }
    }

    // Log util
    private static void log(String message) {
        System.out.println("[Hooks] " + message);
    }
}
