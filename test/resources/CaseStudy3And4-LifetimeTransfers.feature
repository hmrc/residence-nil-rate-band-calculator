Feature: Case Study 3

  Scenario: 3.1 - Case Study 3
    When I POST these details to calculate
      | dateOfDeath                | 2021-01-01 |
      | chargeableTransferAmount   | 450000     |
      | grossEstateValue           | 450000     |
      | propertyValue              | 200000     |
      | percentageCloselyInherited | 100        |
      | broughtForwardAllowance    | 0          |
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 175000 |
      | carryForwardAmount          | 0      |

  Scenario: 4.1 - Case Study 4
    When I POST these details to calculate
      | dateOfDeath                | 2021-01-01 |
      | chargeableTransferAmount   | 750000     |
      | grossEstateValue           | 750000     |
      | propertyValue              | 500000     |
      | percentageCloselyInherited | 100        |
      | broughtForwardAllowance    | 0          |
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 175000 |
      | carryForwardAmount          | 0      |
