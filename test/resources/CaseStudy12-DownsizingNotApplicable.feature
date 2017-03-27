@CaseStudy12
@CaseStudy
Feature: Case Study 12

  Variants on Case Study 12, a case where downsizing is not due

  Scenario: 11.1 - Case Study 11
    When I combine these details
      | dateOfDeath                | 2020-08-01 |
      | chargeableEstateValue   | 700000     |
      | valueOfEstate              | 700000     |
      | propertyValue              | 200000     |
      | percentagePassedToDirectDescendants | 0          |
      | valueBeingTransferred    | 0          |
    And these downsizing details
      | datePropertyWasChanged                    | 2018-10-01 |
      | valueOfChangedProperty           | 450000     |
      | valueOfAssetsPassing             | 500000     |
      | valueAvailableWhenPropertyChanged | 0          |
    And POST the details to calculate
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 0      |
      | carryForwardAmount          | 175000 |
      | defaultAllowanceAmount      | 175000 |
      | adjustedAllowanceAmount     | 175000 |
