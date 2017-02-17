@CaseStudy23
@CaseStudy
@Downsizing
Feature: Case Study 23

  Variants on Case Study 23, downsizing and leaving a property with brought forward allowance

  Scenario: 23 - Case Study 23
    When I combine these details
      | dateOfDeath                | 2020-05-01 |
      | chargeableTransferAmount   | 800000     |
      | grossEstateValue           | 800000     |
      | propertyValue              | 90000      |
      | percentageCloselyInherited | 100        |
      | broughtForwardAllowance    | 175000     |
    And these downsizing details
      | dateOfDisposal                    | 2018-10-01 |
      | valueOfDisposedProperty           | 500000     |
      | valueCloselyInherited             | 710000     |
      | broughtForwardAllowanceAtDisposal | 125000     |
    And POST the details to calculate
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 350000 |
      | carryForwardAmount          | 0      |
      | defaultAllowanceAmount      | 350000 |
      | adjustedAllowanceAmount     | 350000 |
