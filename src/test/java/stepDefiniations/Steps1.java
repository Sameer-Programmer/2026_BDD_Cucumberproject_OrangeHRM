package stepDefiniations;

import io.cucumber.java.en.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import pageObjects.HomePage;
import pageObjects.LoginPage;

import javax.sql.rowset.WebRowSet;
import java.time.Duration;

public class Steps1 {
    public WebDriver driver;
    public LoginPage loginPage;
    public HomePage homePage;

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

}
