Feature: Persons Former Allowance

  Tests of the calculaton for person's former allowance, which is calculated as:

  RNRB on property change + Value Being Transferred + Excess of Value Being Transferred at death over Value Being Transferred

  Where "excess BFA" = (adjusted BFA) - BFA

  However, Value Being Transferred is reduced to 0 if the property was changed before 6 April 2017

  Scenario Outline: Persons Former Allowance
    When I calculate persons former allowance with <datePropertyWasChanged>, <rnrnOnPropertyChange>, <valueAvailableWhenPropertyChanged> and <adjustedValueBeingTransferred>
    Then the persons former allowance should be <expectedAnswer>

    Examples:
      | datePropertyWasChanged | rnrnOnPropertyChange | valueAvailableWhenPropertyChanged | adjustedValueBeingTransferred | expectedAnswer |
      | 2017-04-05             | 100000               | 50000                             | 0                               | 100000         |
      | 2017-04-06             | 100000               | 50000                             | 0                               | 150000         |
      | 2017-04-06             | 100000               | 50000                             | 50000                           | 150000         |
      | 2017-04-06             | 100000               | 50000                             | 60000                           | 160000         |
