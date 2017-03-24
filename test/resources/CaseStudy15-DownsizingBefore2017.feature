@CaseStudy15
@CaseStudy
@Downsizing
Feature: Case Study 15

  Variants on Case Study 15, downsizing before 6 April 2017

  Scenario: 15.1 - Case Study 15
    When I combine these details
      | dateOfDeath                | 2019-12-01 |
      | chargeableEstateValue   | 80000      |
      | valueOfEstate              | 185000     |
      | propertyValue              | 105000     |
      | percentagePassedToDirectDescendants | 0          |
      | broughtForwardAllowance    | 0          |
    And these property value after exemption details
      | value          | 0 |
      | inheritedValue | 0 |
    And these downsizing details
      | dateOfDisposal                    | 2016-03-01 |
      | valueOfDisposedProperty           | 150000     |
      | valueCloselyInherited             | 80000      |
      | broughtForwardAllowanceAtDisposal | 0          |
    And POST the details to calculate
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 150000 |
      | residenceNilRateAmount      | 45000  |
      | carryForwardAmount          | 105000 |
      | defaultAllowanceAmount      | 150000 |
      | adjustedAllowanceAmount     | 150000 |

  Scenario: 15.2 - Case Study 15A
    When I combine these details
      | dateOfDeath                | 2019-12-01 |
      | chargeableEstateValue   | 80000      |
      | valueOfEstate              | 185000     |
      | propertyValue              | 0          |
      | percentagePassedToDirectDescendants | 0          |
      | broughtForwardAllowance    | 0          |
    And these downsizing details
      | dateOfDisposal                    | 2016-03-01 |
      | valueOfDisposedProperty           | 150000     |
      | valueCloselyInherited             | 10000      |
      | broughtForwardAllowanceAtDisposal | 0          |
    And POST the details to calculate
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 150000 |
      | residenceNilRateAmount      | 10000  |
      | carryForwardAmount          | 140000 |
      | defaultAllowanceAmount      | 150000 |
      | adjustedAllowanceAmount     | 150000 |
