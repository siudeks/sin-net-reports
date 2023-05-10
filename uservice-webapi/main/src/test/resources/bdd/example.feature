Feature: An example

  Scenario: Predefined projects
    When user is requesting list of projects
    Then Project uservice is requested
    And Response is returned
