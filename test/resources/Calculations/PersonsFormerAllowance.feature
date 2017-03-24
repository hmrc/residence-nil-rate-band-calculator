Feature: Persons Former Allowance

  Tests of the calculaton for person's former allowance, which is calculated as:

  RNRB on property change + Brought Forward Allowance + Excess of Brought Forward Allowance at death over Brought Forward Allowance

  Where "excess BFA" = (adjusted BFA) - BFA

  However, brought Forward Allowance is reduced to 0 if the property was changed before 6 April 2017

  Scenario Outline: Persons Former Allowance
    When I calculate persons former allowance with <datePropertyWasChanged>, <rnrnOnPropertyChange>, <valueAvailableWhenPropertyChanged> and <adjustedBroughtForwardAllowance>
    Then the persons former allowance should be <expectedAnswer>

    Examples:
      | datePropertyWasChanged | rnrnOnPropertyChange | valueAvailableWhenPropertyChanged | adjustedBroughtForwardAllowance | expectedAnswer |
      | 2017-04-05             | 100000               | 50000                             | 0                               | 100000         |
      | 2017-04-06             | 100000               | 50000                             | 0                               | 150000         |
      | 2017-04-06             | 100000               | 50000                             | 50000                           | 150000         |
      | 2017-04-06             | 100000               | 50000                             | 60000                           | 160000         |
