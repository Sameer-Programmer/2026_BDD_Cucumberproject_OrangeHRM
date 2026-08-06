package testRunner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
@CucumberOptions(
        features = ".//Features/Login.feature",
        glue = "stepDefiniations",
        dryRun = false,
        monochrome = true,
        plugin = {"pretty", "html:target/cucumber-reports.html", "json:target/cucumber.json"}
)
public class TestCaserun extends AbstractTestNGCucumberTests {
}
