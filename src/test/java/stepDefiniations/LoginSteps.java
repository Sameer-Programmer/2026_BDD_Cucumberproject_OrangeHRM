package stepDefiniations;

import io.cucumber.java.en.*;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.Assert;
import pageObjects.HomePage;
import pageObjects.LoginPage;

import java.io.IOException;
import java.time.Duration;

public class LoginSteps extends Baseclass {

    // ==========================================================
    // Login Steps
    // ==========================================================
    @Given("Launch the Browser")
    public void launch_the_browser() throws IOException {

        loadConfig();

        logger.info("===== LOGGER TEST =====");

        String browser = prop.getProperty("browser").trim().toLowerCase();

        // Read headless value from config.properties
        isHeadless = Boolean.parseBoolean(prop.getProperty("headless", "false"));

        if (browser.equals("chrome")) {

            logger.info("********** Launching Chrome Browser (headless={}) **********", isHeadless);

            ChromeOptions options = new ChromeOptions();

            if (isHeadless) {
                options.addArguments("--headless=new");
                options.addArguments("--window-size=1920,1080");
            }

            driver = new ChromeDriver(options);

        } else if (browser.equals("edge")) {

            logger.info("********** Launching Edge Browser (headless={}) **********", isHeadless);

            EdgeOptions options = new EdgeOptions();

            if (isHeadless) {
                options.addArguments("--headless=new");
                options.addArguments("--window-size=1920,1080");
            }

            driver = new EdgeDriver(options);

        } else if (browser.equals("firefox")) {

            logger.info("********** Launching Firefox Browser (headless={}) **********", isHeadless);

            FirefoxOptions options = new FirefoxOptions();

            if (isHeadless) {
                options.addArguments("-headless");
            }

            driver = new FirefoxDriver(options);

        } else {

            throw new IllegalArgumentException("Invalid Browser Name : " + browser);
        }

        long explicitWaitSeconds =
                Long.parseLong(prop.getProperty("explicitWait", "20"));

        explicitWait = Duration.ofSeconds(explicitWaitSeconds);

        loginPage = new LoginPage(driver);

        logger.info(browser + " Browser Launched Successfully");
    }

    @When("Enter the URL {string}")
    public void enter_the_url(String url) {

        logger.info("Opening URL : {}", url);

        driver.manage().deleteAllCookies();

        if (isHeadless) {
            driver.manage().window().setSize(new Dimension(1920, 1080));
        } else {
            driver.manage().window().maximize();
        }

        long implicitWaitSeconds =
                Long.parseLong(prop.getProperty("implicitWait", "10"));

        driver.manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(implicitWaitSeconds));

        driver.get(url);

        logger.info("Application Opened Successfully");
    }

    @When("Enter the URL from Config")
    public void enter_the_url_from_config() {
        enter_the_url(prop.getProperty("url"));
    }

    @When("User Enters Email as {string} and password as {string} Click on Login")
    public void user_enters_email_as_and_password_as_click_on_login(String username, String password) {

        loginPage.setUserName(username);
        loginPage.setPassword(password);
        loginPage.clickLogin();

        homePage = new HomePage(driver);

        logger.info("Login Successful");
    }

    @When("User Enters Credentials from Config and Click on Login")
    public void user_enters_credentials_from_config_and_click_on_login() {

        String username = prop.getProperty("username");
        String password = prop.getProperty("password");

        logger.info("Logging in with username from config : {}", username);

        loginPage.setUserName(username);
        loginPage.setPassword(password);
        loginPage.clickLogin();

        homePage = new HomePage(driver);

        logger.info("Login Successful");
    }

    @Then("HomScreen Should be {string}")
    public void page_title_should_be(String expectedText) {

        logger.info("Current URL : {}", driver.getCurrentUrl());
        logger.info("Page Title : {}", driver.getTitle());

        if (driver.getPageSource().contains("Invalid credentials")) {
            Assert.fail("Invalid credentials");
        }

        Assert.assertTrue(
                homePage.isDashboardDisplayed(),
                "Dashboard is not displayed");

        Assert.assertTrue(
                driver.getPageSource().contains(expectedText),
                "Expected text is not found");
    }

    @Then("Logout")
    public void logout() {
        homePage.clickLogout();
        logger.info("Logout Successful");
    }

    @Then("close the Browser")
    public void close_the_browser() {

        if (driver != null) {
            driver.quit();
            logger.info("Browser Closed Successfully");
        }
    }


}