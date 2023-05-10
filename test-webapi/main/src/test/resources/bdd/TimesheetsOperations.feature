Feature: Timesheet Operations

  Evidence of Timesheets is the core piece of the functionality, as it is PSA-class project
  
  Background:
    Given a project called project1 created by user1
    And a project called project2 created by user2
    And a person named operator1
    
  Rule:

    @todo
    Scenario: Reject creation of a timesheet by unpermitted person
      When operator1 creates timeentry for project1 owned by user1
      Then operation is rejected
      And number of timesheets in project1 is zero

    @only
    Scenario: Create new timesheet by a project operator
      When User user1 assigns operator1 to project1
      And operator1 creates timeentry for project1 owned by user1
      Then number of timesheets in project1 is zero

