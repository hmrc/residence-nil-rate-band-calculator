@CaseStudy8
@CaseStudy
Feature: Case Study 8

  Variants on Case Study 8, a case with a property being left to a surviving spouse in trust then being left to children on
  the surviving spouse's death.
  The fact the property goes into a trust does not affect RNRB calculations, but this case study is included as a test for completeness.

  Scenario: 8.1 - Case Study 8
    When I POST these details to calculate
      | dateOfDeath                | 2020-12-01 |
      | chargeableEstateValue   | 1250000    |
      | valueOfEstate              | 1200000    |
      | propertyValue              | 400000     |
      | percentagePassedToDirectDescendants | 100        |
      | valueBeingTransferred    | 175000     |
    Then I should get an OK response
    And the response body should be
      | applicableNilRateBandAmount | 175000 |
      | residenceNilRateAmount      | 350000 |
      | carryForwardAmount          | 0      |
      | defaultAllowanceAmount      | 350000 |
      | adjustedAllowanceAmount     | 350000 |
