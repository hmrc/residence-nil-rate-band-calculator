@CaseStudy22
@CaseStudy
@Downsizing
Feature: Case Study 22

  Variants on Case Study 22, downsizing and leaving a property with brought forward allowance

  Scenario: 22.1 - Case Study 22
    When I combine these details
      | dateOfDeath                | 2020-08-01 |
      | chargeableTransferAmount   | 1500000    |
      | grossEstateValue           | 1500000    |
      | propertyValue              | 0          |
      | percentageCloselyInherited | 0          |
      | broughtForwardAllowance    | 0          |
    And these downsizing details
      | dateOfDisposal                    | 2020-05-01 |
      | valueOfDisposedProperty           | 200000     |
      | valueCloselyInherited             | 200000     |
      | broughtForwardAllowanceAtDisposal | 0          |
    And POST the details to calculate
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 175000 |
      | carryForwardAmount          | 0      |
    