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

  Scenario: Customer creation
    When Customer creation request is send to backend
    Then Customer creation result is verified

  Scenario: Users list query
    When Users list query is send
    Then Users list response is returned
