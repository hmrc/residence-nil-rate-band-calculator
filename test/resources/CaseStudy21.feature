@CaseStudy21
@CaseStudy
@Downsizing
Feature: Case Study 21

  Variants on Case Study 21, selling a residence and leaving another

  Scenario: 21.1 - Case Study 21
    When I combine these details
      | dateOfDeath                | 2020-08-01 |
      | chargeableEstateValue   | 1500000    |
      | valueOfEstate              | 1500000    |
      | propertyValue              | 150000     |
      | percentagePassedToDirectDescendants | 100        |
      | broughtForwardAllowance    | 0          |
    And these downsizing details
      | datePropertyWasChanged                    | 2020-05-01 |
      | valueOfChangedProperty           | 200000     |
      | valueCloselyInherited             | 200000     |
      | valueAvailableWhenPropertyChanged | 0          |
    And POST the details to calculate
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 175000 |
      | carryForwardAmount          | 0      |
      | defaultAllowanceAmount      | 175000 |
      | adjustedAllowanceAmount     | 175000 |

  Scenario: 21.2 - Case Study 21A
    When I combine these details
      | dateOfDeath                | 2020-08-01 |
      | chargeableEstateValue   | 1500000    |
      | valueOfEstate              | 1500000    |
      | propertyValue              | 150000     |
      | percentagePassedToDirectDescendants | 0          |
      | broughtForwardAllowance    | 0          |
    And these downsizing details
      | datePropertyWasChanged                    | 2020-05-01 |
      | valueOfChangedProperty           | 200000     |
      | valueCloselyInherited             | 200000     |
      | valueAvailableWhenPropertyChanged | 0          |
    And POST the details to calculate
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000  |
      | residenceNilRateAmount      | 25000   |
      | carryForwardAmount          | 150000  |
      | defaultAllowanceAmount      | 175000  |
      | adjustedAllowanceAmount     | 175000  |
