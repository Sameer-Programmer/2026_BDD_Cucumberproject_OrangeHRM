package stepDefiniations;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import pageObjects.CandidatePage;
import pageObjects.HomePage;
import pageObjects.LoginPage;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.Duration;
import java.util.Properties;

public class Baseclass {

    public WebDriver driver;
    public LoginPage loginPage;
    public HomePage homePage;
    public CandidatePage candidatePage;

    public Properties prop;

    // Logger
    public static final Logger logger = LogManager.getLogger(Baseclass.class);

    // Framework Configuration
    public boolean isHeadless = false;
    public Duration explicitWait;

    public void loadConfig() throws IOException {

        prop = new Properties();

        try (FileInputStream fis = new FileInputStream(
                System.getProperty("user.dir")
                        + "/src/test/resources/config.properties")) {

            prop.load(fis);
        }
    }

    public static String randomString() {
        return RandomStringUtils.randomAlphabetic(5);
    }
}