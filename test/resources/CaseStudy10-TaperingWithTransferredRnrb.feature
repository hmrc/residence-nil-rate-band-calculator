@CaseStudy10
@CaseStudy
Feature: Case Study 10

  Variants on Case Study 10, a case with tapering applied

  Scenario: 10.1 - Case Study 10
    When I POST these details to calculate
      | dateOfDeath                | 2018-05-01 |
      | chargeableTransferAmount   | 2100000    |
      | grossEstateValue           | 2100000    |
      | propertyValue              | 450000     |
      | percentageCloselyInherited | 0          |
      | broughtForwardAllowance    | 0          |
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
      | chargeableTransferAmount   | 1800000    |
      | grossEstateValue           | 1800000    |
      | propertyValue              | 500000     |
      | percentageCloselyInherited | 100        |
      | broughtForwardAllowance    | 105000     |
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 280000 |
      | carryForwardAmount          | 0      |
      | defaultAllowanceAmount      | 280000 |
      | adjustedAllowanceAmount     | 280000 |
