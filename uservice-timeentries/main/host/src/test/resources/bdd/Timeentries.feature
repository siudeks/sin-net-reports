Feature: Manage Timeentries

  Background:
    Given logged user called user1
    And known user called operator1
    And a new project called project1 is created by user1
    And an operator called operator1 assigned to project called project1

  Scenario: Create a new Timeentry
    When the operator1 creates new timeentry
    And the new timeentry is visible on the project1
