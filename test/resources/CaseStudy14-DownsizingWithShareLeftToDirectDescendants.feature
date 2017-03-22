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
      | percentageCloselyInherited | 50         |
      | broughtForwardAllowance    | 0          |
    And these property value after exemption details
      | value                 | 52500 |
      | valueCloselyInherited | 52500 |
    And these downsizing details
      | dateOfDisposal                    | 2019-02-01 |
      | valueOfDisposedProperty           | 400000     |
      | valueCloselyInherited             | 150000     |
      | broughtForwardAllowanceAtDisposal | 0          |
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
      | percentageCloselyInherited | 50         |
      | broughtForwardAllowance    | 0          |
    And these property value after exemption details
      | value                 | 52500 |
      | valueCloselyInherited | 52500 |
    And these downsizing details
      | dateOfDisposal                    | 2019-02-01 |
      | valueOfDisposedProperty           | 400000     |
      | valueCloselyInherited             | 20000      |
      | broughtForwardAllowanceAtDisposal | 0          |
    And POST the details to calculate
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 72500  |
      | carryForwardAmount          | 102500 |
      | defaultAllowanceAmount      | 175000 |
      | adjustedAllowanceAmount     | 175000 |
