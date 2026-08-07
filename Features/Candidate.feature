Feature: Candidates

  Scenario: Add a Candidate
    Given Launch the Chrome Browser
    When Enter the URL "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login"
    And User Enters Email as "Admin" and password as "admin123" Click on Login
    Then HomScreen Should be "Dashboard"
    When User Clicks on Recruitment Menu
    And User Clicks on Add Button
    When User enter the Candidate Details
    And Click on Save button
    Then user can view the confirmation message "Successfully Saved"
    Then close the Browser


  Scenario: Search Candidate by Vacancy
    Given Launch the Chrome Browser
    When Enter the URL "https://opensource-demo.orangehrmlive.com/web/index.php/auth/login"
    And User Enters Email as "Admin" and password as "admin123" Click on Login
    Then HomScreen Should be "Dashboard"
    When User Clicks on Recruitment Menu
    Then Candidate Should be "Candidates"
    And Click on Senior QA lead from Vacency dropdown
    And Click on Search button
    Then CandidateName should be displayed in the Table
