package stepDefiniations;

import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import pageObjects.CandidatePage;
import pageObjects.HomePage;
import pageObjects.LoginPage;

import javax.sql.rowset.WebRowSet;
import java.time.Duration;

public class Steps1 extends Baseclass{


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
        } else {
            Assert.assertTrue(homePage.isDashboardDisplayed(), "Dashboard is not displayed");
            Assert.assertTrue(driver.getPageSource().contains(expectedText));
        }
    }

    @Then("Logout")
    public void logout() {
        homePage.clickLogout();
    }

    @Then("close the Browser")
    public void close_the_browser() {
        driver.quit();
    }
    // Additinal

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
        String email = randomString()+"@yopmail.com";
        candidatePage.enterEmail(email);
        candidatePage.enterContactNumber("9999999999");
    }
    @When("Click on Save button")
    public void click_on_save_button() {
        candidatePage.clickSaveButton();
    }
    @Then("user can view the confirmation message {string}")
    public void user_can_view_the_confirmation_message(String expectedText) {
        Assert.assertTrue(candidatePage.isSuccessMessageDisplayed(), "Success is not displayed");
        Assert.assertEquals(candidatePage.getSuccessMessage(), expectedText,
                "Success message is incorrect");

    }





}
