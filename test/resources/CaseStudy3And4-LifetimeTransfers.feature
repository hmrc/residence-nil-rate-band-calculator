@CaseStudy3
@CaseStudy
Feature: Case Study 3

  @CaseStudy3
  Scenario: 3.1 - Case Study 3
    When I POST these details to calculate
      | dateOfDeath                | 2021-01-01 |
      | chargeableEstateValue   | 450000     |
      | valueOfEstate              | 450000     |
      | propertyValue              | 200000     |
      | percentagePassedToDirectDescendants | 100        |
      | valueBeingTransferred    | 0          |
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 175000 |
      | carryForwardAmount          | 0      |
      | defaultAllowanceAmount      | 175000 |
      | adjustedAllowanceAmount     | 175000 |

  @CaseStudy4
  Scenario: 4.1 - Case Study 4
    When I POST these details to calculate
      | dateOfDeath                | 2021-01-01 |
      | chargeableEstateValue   | 750000     |
      | valueOfEstate              | 750000     |
      | propertyValue              | 500000     |
      | percentagePassedToDirectDescendants | 100        |
      | valueBeingTransferred    | 0          |
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 175000 |
      | carryForwardAmount          | 0      |
      | defaultAllowanceAmount      | 175000 |
      | adjustedAllowanceAmount     | 175000 |
