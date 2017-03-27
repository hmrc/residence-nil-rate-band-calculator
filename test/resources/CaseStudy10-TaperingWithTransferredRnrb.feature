@CaseStudy10
@CaseStudy
Feature: Case Study 10

  Variants on Case Study 10, a case with tapering applied

  Scenario: 10.1 - Case Study 10
    When I POST these details to calculate
      | dateOfDeath                | 2018-05-01 |
      | chargeableEstateValue   | 2100000    |
      | valueOfEstate              | 2100000    |
      | propertyValue              | 450000     |
      | percentagePassedToDirectDescendants | 0          |
      | valueBeingTransferred    | 0          |
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 125000 |
      | residenceNilRateAmount      | 0      |
      | carryForwardAmount          | 75000  |
      | defaultAllowanceAmount      | 125000 |
      | adjustedAllowanceAmount     | 75000  |

  Scenario: 10.2 - Case Study 10A
    When I POST these details to calculate
      | dateOfDeath                | 2021-03-25 |
      | chargeableEstateValue   | 1800000    |
      | valueOfEstate              | 1800000    |
      | propertyValue              | 500000     |
      | percentagePassedToDirectDescendants | 100        |
      | valueBeingTransferred    | 105000     |
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 280000 |
      | carryForwardAmount          | 0      |
      | defaultAllowanceAmount      | 280000 |
      | adjustedAllowanceAmount     | 280000 |
