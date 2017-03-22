@CaseStudy7
@CaseStudy
Feature: Case Study 7

  Variants on Case Study 7, a simple case with the property being sold as part of the administration of the estate.
  The sale of the property does not affect RNRB calculations, but this case study is included as a test for completeness.

  Scenario: 7.1 - Case Study 7
    When I POST these details to calculate
      | dateOfDeath                | 2019-12-01 |
      | chargeableEstateValue   | 900000     |
      | valueOfEstate              | 900000     |
      | propertyValue              | 500000     |
      | percentagePassedToDirectDescendants | 100        |
      | broughtForwardAllowance    | 0          |
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 150000 |
      | residenceNilRateAmount      | 150000 |
      | carryForwardAmount          | 0      |
      | defaultAllowanceAmount      | 150000 |
      | adjustedAllowanceAmount     | 150000 |
