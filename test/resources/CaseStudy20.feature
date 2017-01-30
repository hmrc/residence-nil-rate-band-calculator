@CaseStudy20
@CaseStudy
@Downsizing
Feature: Case Study 20

  Variants on Case Study 20, downsizing and leaving a property with brought forward allowance

  Scenario: 20.1 - Case Study 20
    When I combine these details
      | dateOfDeath                | 2019-12-01 |
      | chargeableTransferAmount   | 1900000    |
      | grossEstateValue           | 1900000    |
      | propertyValue              | 240000     |
      | percentageCloselyInherited | 100        |
      | broughtForwardAllowance    | 150000     |
    And these downsizing details
      | dateOfDisposal                    | 2019-07-01 |
      | valueOfDisposedProperty           | 285000     |
      | valueCloselyInherited             | 1660000    |
      | broughtForwardAllowanceAtDisposal | 150000     |
    And POST the details to calculate
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 150000 |
      | residenceNilRateAmount      | 285000 |
      | carryForwardAmount          | 15000  |
      | defaultAllowanceAmount      | 300000 |
      | adjustedAllowanceAmount     | 300000 |

  Scenario: 20.2 - Case Study 20A
    When I combine these details
      | dateOfDeath                | 2019-12-01 |
      | chargeableTransferAmount   | 1900000    |
      | grossEstateValue           | 1900000    |
      | propertyValue              | 240000     |
      | percentageCloselyInherited | 100        |
      | broughtForwardAllowance    | 150000     |
    And these downsizing details
      | dateOfDisposal                    | 2017-07-01 |
      | valueOfDisposedProperty           | 285000     |
      | valueCloselyInherited             | 1660000    |
      | broughtForwardAllowanceAtDisposal | 100000     |
    And POST the details to calculate
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 150000 |
      | residenceNilRateAmount      | 300000 |
      | carryForwardAmount          | 0      |
      | defaultAllowanceAmount      | 300000 |
      | adjustedAllowanceAmount     | 300000 |
