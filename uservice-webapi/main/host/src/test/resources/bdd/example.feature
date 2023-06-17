Feature: An example

  Scenario: Predefined projects
    When user is requesting list of projects
    Then Project uservice is requested
    And Response is returned

  Scenario: Save project
    When a project is saved
    Then operation result is returned

  Scenario: User stats
    When userstats request is send to backend
    Then userstats are returned
