@CaseStudy2
@CaseStudy
Feature: Case Study 2

  Variants on Case Study 2, a simple case where the home is worth less than the maximum RNRB

  Scenario: 2.1 - Case Study 2
    When I POST these details to calculate
      | dateOfDeath                | 2021-01-01 |
      | chargeableEstateValue   | 500000     |
      | valueOfEstate              | 1000000    |
      | propertyValue              | 100000     |
      | percentagePassedToDirectDescendants | 100        |
      | valueBeingTransferred    | 0          |
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 100000 |
      | carryForwardAmount          | 75000  |
      | defaultAllowanceAmount      | 175000 |
      | adjustedAllowanceAmount     | 175000 |

  Scenario: 2.2 - Death occurs in an earlier tax year
    When I POST these details to calculate
      | dateOfDeath                | 2019-01-01 |
      | chargeableEstateValue   | 500000     |
      | valueOfEstate              | 1000000    |
      | propertyValue              | 100000     |
      | percentagePassedToDirectDescendants | 100        |
      | valueBeingTransferred    | 0          |
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 125000 |
      | residenceNilRateAmount      | 100000 |
      | carryForwardAmount          | 25000  |
      | defaultAllowanceAmount      | 125000 |
      | adjustedAllowanceAmount     | 125000 |
