@CaseStudy19
@CaseStudy
@Downsizing
Feature: Case Study 19

  Variants on Case Study 19, downsizing and leaving a property with brought forward allowance

  Scenario: 19.1 - Case Study 19
    When I combine these details
      | dateOfDeath                | 2020-05-01 |
      | chargeableEstateValue   | 635000     |
      | valueOfEstate              | 635000     |
      | propertyValue              | 210000     |
      | percentagePassedToDirectDescendants | 100        |
      | broughtForwardAllowance    | 175000     |
    And these downsizing details
      | dateOfDisposal                    | 2015-09-01 |
      | valueOfDisposedProperty           | 300000     |
      | valueCloselyInherited             | 425000     |
      | broughtForwardAllowanceAtDisposal | 0          |
    And POST the details to calculate
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 350000 |
      | carryForwardAmount          | 0      |
      | defaultAllowanceAmount      | 350000 |
      | adjustedAllowanceAmount     | 350000 |
