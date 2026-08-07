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
import pageObjects.CandidatePage;
import pageObjects.HomePage;
import pageObjects.LoginPage;

import java.io.IOException;
import java.time.Duration;

public class Steps1 extends Baseclass {

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

    // ==========================================================
    // Candidate Steps
    // ==========================================================

    // ==========================================================
// Candidate Steps
// ==========================================================

    @When("User Clicks on Recruitment Menu")
    public void user_clicks_on_recruitment_menu() {

        logger.info("Clicking on Recruitment Menu");

        homePage.clickRecruitment();
        candidatePage = new CandidatePage(driver);

        logger.info("Recruitment Page Opened Successfully");
    }

    @When("User Clicks on Add Button")
    public void user_clicks_on_add_button() {

        logger.info("Clicking on Add Button");

        candidatePage.clickAddButton();

        logger.info("Add Candidate Page Opened Successfully");
    }

    @When("User enter the Candidate Details")
    public void user_enter_the_candidate_details() {

        logger.info("Entering Candidate Details");

        candidatePage.enterFirstName("Sameer");
        candidatePage.enterMiddleName("Mohammed");
        candidatePage.enterLastName("Shaik");

        candidatePage.clickVacancyDropdown();
        candidatePage.selectSeniorQALead();

        String email = randomString() + "@yopmail.com";
        logger.info("Generated Email : {}", email);

        candidatePage.enterEmail(email);
        candidatePage.enterContactNumber("9999999999");

        logger.info("Candidate Details Entered Successfully");
    }

    @When("Click on Save button")
    public void click_on_save_button() {

        logger.info("Clicking on Save Button");

        candidatePage.clickSaveButton();
    }

    @Then("user can view the confirmation message {string}")
    public void user_can_view_the_confirmation_message(String expectedText) {

        Assert.assertTrue(
                candidatePage.isSuccessMessageDisplayed(),
                "Success message is not displayed");

        Assert.assertEquals(
                candidatePage.getSuccessMessage(),
                expectedText,
                "Success message is incorrect");

        logger.info("Candidate Saved Successfully");
    }

    @Then("Candidate Should be {string}")
    public void candidate_should_be(String expectedText) {

        Assert.assertTrue(
                candidatePage.isCandidatesHeaderDisplayed(),
                "Candidates page is not displayed");

        Assert.assertEquals(
                candidatePage.getCandidatesHeaderText(),
                expectedText,
                "Candidates page header is incorrect");

        logger.info("Candidates Page Verified Successfully");
    }

    @And("Click on Senior QA lead from Vacancy dropdown")
    public void click_on_senior_qa_lead_from_vacancy_dropdown() {

        logger.info("Selecting Vacancy : Senior QA Lead");

        candidatePage.clickVacancyDropdown();
        candidatePage.selectSeniorQALead();
    }

    @Then("Click on Search button")
    public void click_on_search_button() {

        logger.info("Clicking on Search Button");

        candidatePage.clickSearchButton();
        candidatePage.moveToTable();

        logger.info("Search Completed Successfully");
    }

    @Then("CandidateName should be displayed in the Table")
    public void candidate_name_should_be_displayed_in_the_table() {

        Assert.assertTrue(
                candidatePage.verifyCandidate(
                        "Senior QA Lead",
                        "Sameer Mohammed Shaik"),
                "Candidate is not displayed in the table");

        logger.info("Candidate Verified Successfully in Search Results");
    }

// ==========================================================
// Table Helper Steps (Learning Purpose)
// ==========================================================
}