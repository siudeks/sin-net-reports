Feature: Project Management

  Projects may be managed but its owners
  TODO:
  - security - users should not be able to see / update other projects
  - overlapping: user should see others projects if is assigned
  
  Rules:
  * User may create and delete its projects
  * Number of free accounts is limited to 10 projects
  * Well-known list of predefined projects are created

  Background:
    Given a person named user1
    And a person named user2
    
  @todo
  Rule:
    Scenario: Create new project
      When User user1 creates new project
      Then Number of projects is 1
      And the project is visible on the list of projects

    Scenario: Delete added project
      When User user2 creates new project
      And User user2 deletes lastly created project
      Then Number of projects is 0

    Scenario: Limit number of projects
      When user1 creates maximum of free projects
      Then The user can't create more projects
