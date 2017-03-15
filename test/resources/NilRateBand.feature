@NilRateBand
Feature: Nil Rate Band

  Scenario: 1 - Correct value is given for 5 April 2017
    When I GET the nil rate band for 2017-04-05
    Then I should get an OK response
    And the band I get back should be 100000

  Scenario: 2 - Correct value is given for 6 April 2017
    When I GET the nil rate band for 2017-04-06
    Then I should get an OK response
    And the band I get back should be 100000

  Scenario: 3 - Correct value is given for 1 Jan 2021
    When I GET the nil rate band for 2021-01-01
    Then I should get an OK response
    And the band I get back should be 175000
