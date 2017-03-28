@CaseStudy6
@CaseStudy
Feature: Case Study 6

  Variants on Case Study 6, a simple case where only part of the property is left to direct descendants

  Scenario: 6.1 - Case Study 6
    When I POST these details to calculate
      | dateOfDeath                | 2021-01-01 |
      | chargeableEstateValue   | 800000     |
      | valueOfEstate              | 800000     |
      | propertyValue              | 500000     |
      | percentagePassedToDirectDescendants | 50.01         |
      | valueBeingTransferred    | 0          |
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 175000 |
      | carryForwardAmount          | 0      |
      | defaultAllowanceAmount      | 175000 |
      | adjustedAllowanceAmount     | 175000 |

  Scenario: 6.2 - Amount left to direct descendants is less than the maximum RNRB
    When I POST these details to calculate
      | dateOfDeath                | 2021-01-01 |
      | chargeableEstateValue   | 750000     |
      | valueOfEstate              | 750000     |
      | propertyValue              | 250000     |
      | percentagePassedToDirectDescendants | 50.01         |
      | valueBeingTransferred    | 0          |
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 125025 |
      | carryForwardAmount          | 49975  |
      | defaultAllowanceAmount      | 175000 |
      | adjustedAllowanceAmount     | 175000 |
