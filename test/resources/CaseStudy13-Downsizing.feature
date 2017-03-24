@CaseStudy13
@CaseStudy
@Downsizing
Feature: Case Study 13

  Variants on Case Study 13, a simple example of downsizing

  Scenario: 13.1 - Case Study 13
    When I combine these details
      | dateOfDeath                | 2020-09-01 |
      | chargeableEstateValue   | 305000     |
      | valueOfEstate              | 305000     |
      | propertyValue              | 105000     |
      | percentagePassedToDirectDescendants | 100        |
      | broughtForwardAllowance    | 0          |
    And these downsizing details
      | datePropertyWasChanged                    | 2018-05-01 |
      | valueOfChangedProperty           | 500000     |
      | valueCloselyInherited             | 200000     |
      | broughtForwardAllowanceAtDisposal | 0          |
    And POST the details to calculate
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 175000 |
      | carryForwardAmount          | 0      |
      | defaultAllowanceAmount      | 175000 |
      | adjustedAllowanceAmount     | 175000 |

  Scenario: 13.2 - Case Study 13A
    When I combine these details
      | dateOfDeath                | 2020-09-01 |
      | chargeableEstateValue   | 305000     |
      | valueOfEstate              | 305000     |
      | propertyValue              | 105000     |
      | percentagePassedToDirectDescendants | 100        |
      | broughtForwardAllowance    | 0          |
    And these downsizing details
      | datePropertyWasChanged                    | 2018-05-01 |
      | valueOfChangedProperty           | 500000     |
      | valueCloselyInherited             | 50000      |
      | broughtForwardAllowanceAtDisposal | 0          |
    And POST the details to calculate
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 155000 |
      | carryForwardAmount          | 20000  |
      | defaultAllowanceAmount      | 175000 |
      | adjustedAllowanceAmount     | 175000 |
