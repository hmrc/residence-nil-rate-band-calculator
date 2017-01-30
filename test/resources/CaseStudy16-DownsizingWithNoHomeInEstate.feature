@CaseStudy16
@CaseStudy
@Downsizing
Feature: Case Study 16

  Variants on Case Study 16, downsizing with no property remaining in the estate

  Scenario: 16.1 - Case Study 16
    When I combine these details
      | dateOfDeath                | 2021-03-01 |
      | chargeableTransferAmount   | 500000     |
      | grossEstateValue           | 500000     |
      | propertyValue              | 0          |
      | percentageCloselyInherited | 0          |
      | broughtForwardAllowance    | 0          |
    And these downsizing details
      | dateOfDisposal                    | 2018-10-01 |
      | valueOfDisposedProperty           | 285000     |
      | valueCloselyInherited             | 250000     |
      | broughtForwardAllowanceAtDisposal | 0          |
    And POST the details to calculate
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 175000 |
      | carryForwardAmount          | 0      |
      | defaultAllowanceAmount      | 175000 |
      | adjustedAllowanceAmount     | 175000 |

  Scenario: 16.2 - Case Study 16A
    When I combine these details
      | dateOfDeath                | 2021-03-01 |
      | chargeableTransferAmount   | 500000     |
      | grossEstateValue           | 500000     |
      | propertyValue              | 0          |
      | percentageCloselyInherited | 0          |
      | broughtForwardAllowance    | 0          |
    And these downsizing details
      | dateOfDisposal                    | 2018-10-01 |
      | valueOfDisposedProperty           | 285000     |
      | valueCloselyInherited             | 100000     |
      | broughtForwardAllowanceAtDisposal | 0          |
    And POST the details to calculate
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 100000 |
      | carryForwardAmount          | 75000  |
      | defaultAllowanceAmount      | 175000 |
      | adjustedAllowanceAmount     | 175000 |
