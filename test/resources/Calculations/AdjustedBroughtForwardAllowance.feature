Feature: Adjusted Value Being Transferred

  Tests of the calculation for adjusted Value Being Transferred.

  Adjusted Value Being Transferred is calculated as:
  Value Being Transferred - (amount to taper * (Value Being Transferred / total allowance)).

  It cannot be negative, so is set to zero if the above equation is negative.


  Scenario Outline: Adjusted Value Being Transferred
    When I calculate adjusted value being transferred with <totalAllowance>, <amountToTaper> and <valueBeingTransferred>
    Then the adjusted Value Being Transferred should be <expectedAnswer>

    Examples:
      | totalAllowance | amountToTaper | valueBeingTransferred | expectedAnswer |
      | 100000         | 0             | 50000                 | 50000          |
      | 100000         | 0             | 0                     | 0              |
      | 100000         | 50000         | 30000                 | 15000          |
      | 100000         | 50000         | 0                     | 0              |
      | 100000         | 100000        | 30000                 | 0              |
      | 100000         | 200000        | 30000                 | 0              |
