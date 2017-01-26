@CaseStudy6
@CaseStudy
Feature: Case Study 6

  Variants on Case Study 6, a simple case where only part of the property is left to direct descendants

  Scenario: 6.1 - Case Study 6
    When I POST these details to calculate
      | dateOfDeath                | 2021-01-01 |
      | chargeableTransferAmount   | 800000     |
      | grossEstateValue           | 800000     |
      | propertyValue              | 500000     |
      | percentageCloselyInherited | 50         |
      | broughtForwardAllowance    | 0          |
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 175000 |
      | carryForwardAmount          | 0      |

  Scenario: 6.2 - Amount left to direct descendants is less than the maximum RNRB
    When I POST these details to calculate
      | dateOfDeath                | 2021-01-01 |
      | chargeableTransferAmount   | 750000     |
      | grossEstateValue           | 750000     |
      | propertyValue              | 250000     |
      | percentageCloselyInherited | 50         |
      | broughtForwardAllowance    | 0          |
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 125000 |
      | carryForwardAmount          | 50000  |
