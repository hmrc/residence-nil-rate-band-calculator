@CaseStudy9
@CaseStudy
Feature: Case Study 9

  Variants on Case Study 9, a case with tapering applied

  Scenario: 9.1 - Case Study 9
    When I POST these details to calculate
      | dateOfDeath                | 2018-12-01 |
      | chargeableTransferAmount   | 2100000    |
      | grossEstateValue           | 2100000    |
      | propertyValue              | 450000     |
      | percentageCloselyInherited | 100        |
      | broughtForwardAllowance    | 0          |
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 125000 |
      | residenceNilRateAmount      | 75000  |
      | carryForwardAmount          | 0      |
      | defaultAllowanceAmount      | 125000 |
      | adjustedAllowanceAmount     | 75000  |
