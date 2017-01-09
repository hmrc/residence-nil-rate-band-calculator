@CaseStudy15
@CaseStudy
@Downsizing
Feature: Case Study 15

  Variants on Case Study 15, downsizing before 6 April 2017

  Scenario: 15.1 - Case Study 15
    When I combine these details
      | dateOfDeath                | 2019-12-01 |
      | chargeableTransferAmount   | 80000      |
      | grossEstateValue           | 185000     |
      | propertyValue              | 105000     |
      | percentageCloselyInherited | 0          |
      | broughtForwardAllowance    | 0          |
    And these downsizing details
      | dateOfDisposal                    | 2016-03-01 |
      | valueOfDisposedProperty           | 300000     |
      | valueCloselyInherited             | 80000      |
      | broughtForwardAllowanceAtDisposal | 0          |
    And POST the details to calculate
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 150000 |
      | residenceNilRateAmount      | 45000  |
      | carryForwardAmount          | 105000 |

  Scenario: 15.2 - Downsizing before 8th July 2015
    When I combine these details
      | dateOfDeath                | 2019-12-01 |
      | chargeableTransferAmount   | 80000      |
      | grossEstateValue           | 185000     |
      | propertyValue              | 105000     |
      | percentageCloselyInherited | 0          |
      | broughtForwardAllowance    | 0          |
    And these downsizing details
      | dateOfDisposal                    | 2015-07-07 |
      | valueOfDisposedProperty           | 300000     |
      | valueCloselyInherited             | 80000      |
      | broughtForwardAllowanceAtDisposal | 0          |
    And POST the details to calculate
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 150000 |
      | residenceNilRateAmount      | 0      |
      | carryForwardAmount          | 150000 |
