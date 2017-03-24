@CaseStudy17
@CaseStudy
@Downsizing
Feature: Case Study 17

  Variants on Case Study 17, downsizing from a property which was worth less than the available RNRB

  Scenario: 17.1 - Case Study 17
    When I combine these details
      | dateOfDeath                | 2021-01-01 |
      | chargeableEstateValue   | 600000     |
      | valueOfEstate              | 600000     |
      | propertyValue              | 0          |
      | percentagePassedToDirectDescendants | 0          |
      | broughtForwardAllowance    | 0          |
    And these downsizing details
      | datePropertyWasChanged                    | 2019-05-01 |
      | valueOfChangedProperty           | 90000      |
      | valueCloselyInherited             | 600000     |
      | valueAvailableWhenPropertyChanged | 0          |
    And POST the details to calculate
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 105000 |
      | carryForwardAmount          | 70000  |
      | defaultAllowanceAmount      | 175000 |
      | adjustedAllowanceAmount     | 175000 |


  Scenario: 17.2 - Alternative scenario with a small estate left to a direct descendant
    When I combine these details
      | dateOfDeath                | 2021-01-01 |
      | chargeableEstateValue   | 100000     |
      | valueOfEstate              | 100000     |
      | propertyValue              | 0          |
      | percentagePassedToDirectDescendants | 0          |
      | broughtForwardAllowance    | 0          |
    And these downsizing details
      | datePropertyWasChanged                    | 2019-05-01 |
      | valueOfChangedProperty           | 90000      |
      | valueCloselyInherited             | 100000     |
      | valueAvailableWhenPropertyChanged | 0          |
    And POST the details to calculate
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 100000 |
      | carryForwardAmount          | 75000  |
      | defaultAllowanceAmount      | 175000 |
      | adjustedAllowanceAmount     | 175000 |
