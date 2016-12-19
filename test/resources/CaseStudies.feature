Feature: Case Studies

  Scenario: Case Study 1
    When I POST these details to calculate
      | dateOfDeath                | 2021-01-01 |
      | chargeableTransferAmount   | 490000     |
      | grossEstateValue           | 490000     |
      | propertyValue              | 300000     |
      | percentageCloselyInherited | 100        |
    Then I should get an OK response
    And the response body should be
      | residenceNilRateAmount | 175000 |
      | carryForwardAmount     | 0      |
