package api.test;

import org.junit.platform.suite.api.ConfigurationParameter;
import org.junit.platform.suite.api.IncludeEngines;
import org.junit.platform.suite.api.SelectClasspathResource;
import org.junit.platform.suite.api.Suite;
import io.cucumber.junit.platform.engine.Constants;

@Suite
@IncludeEngines("cucumber")
@SelectClasspathResource("resources/api")  // Sesuaikan dengan lokasi file .feature
@ConfigurationParameter(key = Constants.FILTER_TAGS_PROPERTY_NAME, value = "@api")
@ConfigurationParameter(key = Constants.GLUE_PROPERTY_NAME, value = "api.steps")
@ConfigurationParameter(key = Constants.PLUGIN_PROPERTY_NAME, value = "pretty, html:target/cucumber-reports/api/cucumber-reports-api-test.html")

public class APItest {

}
