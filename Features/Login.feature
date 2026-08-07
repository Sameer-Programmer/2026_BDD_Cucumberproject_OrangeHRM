Feature: Login Feature

  Scenario: Login with Valid Credentials
    Given Launch the Browser
    When Enter the URL from Config
    And User Enters Credentials from Config and Click on Login
    Then HomScreen Should be "Dashboard"
    Then Logout
    Then close the Browser


  Scenario Outline: Login with Multiple Credentials

    Given Launch the Browser
    When Enter the URL from Config
    And User Enters Email as "<username>" and password as "<password>" Click on Login
    Then HomScreen Should be "Dashboard"
    Then Logout
    Then close the Browser

    Examples:
      | username | password |
      | Admin    | admin123 |
      | Admin1   | admin123 |
