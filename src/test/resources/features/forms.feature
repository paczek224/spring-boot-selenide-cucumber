Feature: Forms

  Scenario: Fill first name field
    Given I open the demo page
    When I fill the first name with "First name"
    Then the first name field should have value "First name"

  Scenario: Fill last name field
    Given I open the demo page
    When I fill the last name with "Last name"
    Then the last name field should have value "Last name"
