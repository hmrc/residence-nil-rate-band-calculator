Feature: Adjusted Brought Forward Allowance

  Tests of the calculation for adjusted brought forward allowance.

  Adjusted brought forward allowance is calculated as:
    brought forward allowance - (amount to taper * (brought forward allowance / total allowance)).

  It cannot be negative, so is set to zero if the above equation is negative.



  Scenario Outline: Adjusted Brought Forward Allowance
    When I calculate adjusted brought forwards allowance with <totalAllowance>, <amountToTaper> and <broughtForwardAllowance>
    Then the adjusted brought forward allowance should be <expectedAnswer>

    Examples:
      | totalAllowance | amountToTaper | broughtForwardAllowance | expectedAnswer |
      | 100000         | 0             | 50000                   | 50000          |
      | 100000         | 0             | 0                       | 0              |
      | 100000         | 50000         | 30000                   | 15000          |
      | 100000         | 50000         | 0                       | 0              |
      | 100000         | 100000        | 30000                   | 0              |
      | 100000         | 200000        | 30000                   | 0              |
