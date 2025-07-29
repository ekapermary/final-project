package runner;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:reports/cucumberAPI-report.html"},
        glue = {"api.steps"},
        features = {"src/test/resources/api"}
        //monochrome = true
)
public class CucumberRunAPI{
    // Kelas ini akan menjalankan semua skenario yang ada di dalam file .feature
}

