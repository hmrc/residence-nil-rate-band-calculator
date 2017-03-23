@CaseStudy5
@CaseStudy
Feature: Case Study 5

  Variants on Case Study 5, a simple case with transfer of unused RNRB

  Scenario: 5.1 - Case Study 5
    When I POST these details to calculate
      | dateOfDeath                | 2019-07-30 |
      | chargeableEstateValue   | 1000000    |
      | valueOfEstate              | 1000000    |
      | propertyValue              | 400000     |
      | percentagePassedToDirectDescendants | 100        |
      | broughtForwardAllowance    | 150000     |
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 150000 |
      | residenceNilRateAmount      | 300000 |
      | carryForwardAmount          | 0      |
      | defaultAllowanceAmount      | 300000 |
      | adjustedAllowanceAmount     | 300000 |

  Scenario: 5.2 - Property worth less than the RNRB + unused allowance
    When I POST these details to calculate
      | dateOfDeath                | 2019-07-30 |
      | chargeableEstateValue   | 750000     |
      | valueOfEstate              | 750000     |
      | propertyValue              | 250000     |
      | percentagePassedToDirectDescendants | 100        |
      | broughtForwardAllowance    | 150000     |
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 150000 |
      | residenceNilRateAmount      | 250000 |
      | carryForwardAmount          | 50000  |
      | defaultAllowanceAmount      | 300000 |
      | adjustedAllowanceAmount     | 300000 |
