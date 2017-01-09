@CaseStudy10
@CaseStudy
Feature: Case Study 10

  Variants on Case Study 10, a case with tapering applied and RNRB being transferred

  Scenario: 10.1 - Case Study 10
    When I POST these details to calculate
      | dateOfDeath                | 2020-12-01 |
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

  Scenario: 10.2 - Variant where the final estate is also subject to tapering
    When I POST these details to calculate
      | dateOfDeath                | 2020-12-01 |
      | chargeableTransferAmount   | 2100000    |
      | grossEstateValue           | 2100000    |
      | propertyValue              | 500000     |
      | percentageCloselyInherited | 100        |
      | broughtForwardAllowance    | 105000     |
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 230000 |
      | carryForwardAmount          | 0      |
