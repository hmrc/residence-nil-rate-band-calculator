@CaseStudy14
@CaseStudy
@Downsizing
Feature: Case Study 14

  Variants on Case Study 14, downsizing with only part of the property left to direct descendants

  Scenario: 14.1 - Case Study 14
    When I combine these details
      | dateOfDeath                | 2020-09-01 |
      | chargeableEstateValue   | 700000     |
      | valueOfEstate              | 700000     |
      | propertyValue              | 105000     |
      | percentagePassedToDirectDescendants | 50         |
      | broughtForwardAllowance    | 0          |
    And these property value after exemption details
      | value          | 52500 |
      | inheritedValue | 52500 |
    And these downsizing details
      | datePropertyWasChanged                    | 2019-02-01 |
      | valueOfChangedProperty           | 400000     |
      | valueCloselyInherited             | 150000     |
      | valueAvailableWhenPropertyChanged | 0          |
    And POST the details to calculate
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 122500 |
      | carryForwardAmount          | 52500  |
      | defaultAllowanceAmount      | 175000 |
      | adjustedAllowanceAmount     | 175000 |

  Scenario: 14.2 - Case Study 14A
    When I combine these details
      | dateOfDeath                | 2020-09-01 |
      | chargeableEstateValue   | 700000     |
      | valueOfEstate              | 700000     |
      | propertyValue              | 105000     |
      | percentagePassedToDirectDescendants | 50         |
      | broughtForwardAllowance    | 0          |
    And these property value after exemption details
      | value          | 52500 |
      | inheritedValue | 52500 |
    And these downsizing details
      | datePropertyWasChanged                    | 2019-02-01 |
      | valueOfChangedProperty           | 400000     |
      | valueCloselyInherited             | 20000      |
      | valueAvailableWhenPropertyChanged | 0          |
    And POST the details to calculate
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 72500  |
      | carryForwardAmount          | 102500 |
      | defaultAllowanceAmount      | 175000 |
      | adjustedAllowanceAmount     | 175000 |
