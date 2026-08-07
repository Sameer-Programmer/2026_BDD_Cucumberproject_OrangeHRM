Feature: Candidates

  Background:
    Given Launch the Browser
    When Enter the URL from Config
    And User Enters Credentials from Config and Click on Login
    Then HomScreen Should be "Dashboard"

  Scenario: Add a Candidate
    When User Clicks on Recruitment Menu
    And User Clicks on Add Button
    And User enter the Candidate Details
    And Click on Save button
    Then user can view the confirmation message "Successfully Saved"
    And close the Browser

  Scenario: Search Candidate by Vacancy
    When User Clicks on Recruitment Menu
    Then Candidate Should be "Candidates"
    And Click on Senior QA lead from Vacancy dropdown
    And Click on Search button
    Then CandidateName should be displayed in the Table
    And close the Browser