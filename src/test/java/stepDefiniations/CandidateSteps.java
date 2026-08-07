package stepDefiniations;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pageObjects.CandidatePage;

public class CandidateSteps extends Baseclass{
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


}
