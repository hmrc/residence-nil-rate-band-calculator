Feature: Case Study 1

  Variants on Case Study 1, a simple case

  Scenario: 1.1 - Case Study 1
    When I POST these details to calculate
      | dateOfDeath                | 2021-01-01 |
      | chargeableTransferAmount   | 490000     |
      | grossEstateValue           | 490000     |
      | propertyValue              | 300000     |
      | percentageCloselyInherited | 100        |
      | broughtForwardAllowance    | 0          |
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 175000 |
      | carryForwardAmount          | 0      |

  Scenario: 1.2 - Death occurs in an earlier tax year
    When I POST these details to calculate
      | dateOfDeath                | 2018-01-01 |
      | chargeableTransferAmount   | 490000     |
      | grossEstateValue           | 490000     |
      | propertyValue              | 300000     |
      | percentageCloselyInherited | 100        |
      | broughtForwardAllowance    | 0          |
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 100000 |
      | residenceNilRateAmount      | 100000 |
      | carryForwardAmount          | 0      |
