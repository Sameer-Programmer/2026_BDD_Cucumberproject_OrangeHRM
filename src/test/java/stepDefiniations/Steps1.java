package stepDefiniations;

import io.cucumber.java.en.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import pageObjects.CandidatePage;
import pageObjects.HomePage;
import pageObjects.LoginPage;

import java.time.Duration;

public class Steps1 extends Baseclass {

    // ==========================================================
    // Login Steps
    // ==========================================================

    @Given("Launch the Chrome Browser")
    public void launch_the_chrome_browser() {
        driver = new ChromeDriver();
        loginPage = new LoginPage(driver);
    }

    @When("Enter the URL {string}")
    public void enter_the_url(String url) {

        driver.manage().deleteAllCookies();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(13));

        driver.get(url);
    }

    @When("User Enters Email as {string} and password as {string} Click on Login")
    public void user_enters_email_as_and_password_as_click_on_login(String username, String password) {

        loginPage.setUserName(username);
        loginPage.setPassword(password);
        loginPage.clickLogin();

        homePage = new HomePage(driver);
    }

    @Then("HomScreen Should be {string}")
    public void page_title_should_be(String expectedText) {

        System.out.println(driver.getCurrentUrl());
        System.out.println(driver.getTitle());

        if (driver.getPageSource().contains("Invalid credentials")) {
            Assert.fail("Invalid credentials");
        }

        Assert.assertTrue(homePage.isDashboardDisplayed(),
                "Dashboard is not displayed");

        Assert.assertTrue(driver.getPageSource().contains(expectedText));
    }

    @Then("Logout")
    public void logout() {
        homePage.clickLogout();
    }

    @Then("close the Browser")
    public void close_the_browser() {
        driver.quit();
    }

    // ==========================================================
    // Candidate Steps
    // ==========================================================

    @When("User Clicks on Recruitment Menu")
    public void user_clicks_on_recruitment_menu() {

        homePage.clickRecruitment();
        candidatePage = new CandidatePage(driver);
    }

    @When("User Clicks on Add Button")
    public void user_clicks_on_add_button() {
        candidatePage.clickAddButton();
    }

    @When("User enter the Candidate Details")
    public void user_enter_the_candidate_details() {

        candidatePage.enterFirstName("Sameer");
        candidatePage.enterMiddleName("Mohammed");
        candidatePage.enterLastName("Shaik");

        candidatePage.clickVacancyDropdown();
        candidatePage.selectSeniorQALead();

        String email = randomString() + "@yopmail.com";

        candidatePage.enterEmail(email);
        candidatePage.enterContactNumber("9999999999");
    }

    @When("Click on Save button")
    public void click_on_save_button() {
        candidatePage.clickSaveButton();
    }

    @Then("user can view the confirmation message {string}")
    public void user_can_view_the_confirmation_message(String expectedText) {

        Assert.assertTrue(candidatePage.isSuccessMessageDisplayed(),
                "Success message is not displayed");

        Assert.assertEquals(candidatePage.getSuccessMessage(),
                expectedText,
                "Success message is incorrect");
    }

    @Then("Candidate Should be {string}")
    public void candidate_should_be(String expectedText) {

        Assert.assertTrue(candidatePage.isCandidatesHeaderDisplayed(),
                "Candidates page is not displayed");

        Assert.assertEquals(candidatePage.getCandidatesHeaderText(),
                expectedText,
                "Candidates page header is incorrect");
    }

    @And("Click on Senior QA lead from Vacancy dropdown")
    public void click_on_senior_qa_lead_from_vacancy_dropdown() {

        candidatePage.clickVacancyDropdown();
        candidatePage.selectSeniorQALead();
    }

    @Then("Click on Search button")
    public void click_on_search_button() {

        candidatePage.clickSearchButton();
        candidatePage.moveToTable();
    }

    @Then("CandidateName should be displayed in the Table")
    public void candidate_name_should_be_displayed_in_the_table() {

        Assert.assertTrue(
                candidatePage.verifyCandidate(
                        "Senior QA Lead",
                        "Sameer Mohammed Shaik"),
                "Candidate is not displayed in the table");
    }

    // ==========================================================
    // Table Helper Steps (Learning Purpose)
    // ==========================================================

}