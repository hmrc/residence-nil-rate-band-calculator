Feature: Case Study 2

  Variants on Case Study 2, a simple case where the home is worth less than the maximum RNRB

  Scenario: 2.1 - Case Study 2
    When I POST these details to calculate
      | dateOfDeath                | 2021-01-01 |
      | chargeableTransferAmount   | 500000     |
      | grossEstateValue           | 1000000    |
      | propertyValue              | 100000     |
      | percentageCloselyInherited | 100        |
      | broughtForwardAllowance    | 0          |
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 100000 |
      | carryForwardAmount          | 75000  |

  Scenario: 2.2 - Death occurs in an earlier tax year
    When I POST these details to calculate
      | dateOfDeath                | 2019-01-01 |
      | chargeableTransferAmount   | 500000     |
      | grossEstateValue           | 1000000    |
      | propertyValue              | 100000     |
      | percentageCloselyInherited | 100        |
      | broughtForwardAllowance    | 0          |
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 125000 |
      | residenceNilRateAmount      | 100000 |
      | carryForwardAmount          | 25000  |
