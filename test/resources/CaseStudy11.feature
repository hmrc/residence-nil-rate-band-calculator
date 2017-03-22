@CaseStudy11
@CaseStudy
Feature: Case Study 11

  Variants on Case Study 11

  Scenario: 11.1 - Case Study 11
    When I combine these details
      | dateOfDeath                | 2020-08-01 |
      | chargeableEstateValue   | 850000     |
      | valueOfEstate              | 850000     |
      | propertyValue              | 0          |
      | percentagePassedToDirectDescendants | 0          |
      | broughtForwardAllowance    | 175000     |
    And these downsizing details
      | dateOfDisposal                    | 2018-06-01 |
      | valueOfDisposedProperty           | 195000     |
      | valueCloselyInherited             | 850000     |
      | broughtForwardAllowanceAtDisposal | 125000     |
    And POST the details to calculate
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 227500 |
      | carryForwardAmount          | 122500 |
      | defaultAllowanceAmount      | 350000 |
      | adjustedAllowanceAmount     | 350000 |
