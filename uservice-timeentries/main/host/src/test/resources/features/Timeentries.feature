Feature: Manage Timeentries

  Background:
    Given a new project called project1
    And an operator called operator1 assigned to project called project1

  Scenario: Create a new Timeentry
    When the operator creates new timeentry
    Then operation succeeded
    And the new timeentry is visible on the project1
