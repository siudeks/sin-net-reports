Feature: Manage Timeentries

  Scenario: Create a new project
    When user creates new project
    Then they can rename the project
    And number of available projects is 1
