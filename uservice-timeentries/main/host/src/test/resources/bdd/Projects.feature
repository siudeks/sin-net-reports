Feature: Manage Timeentries

  Background:
    Given logged user called user1

  Scenario: Create a new project
    When user creates new project named project1
    Then they can rename the project
    And number of available projects is 1
    And the project name is as expected
