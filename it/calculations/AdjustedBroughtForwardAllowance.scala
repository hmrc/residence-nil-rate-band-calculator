package calculations

import helpers.BaseComponentClass

/*
  Tests of the calculation for adjusted Value Being Transferred.

  Adjusted Value Being Transferred is calculated as:
  Value Being Transferred - (amount to taper * (Value Being Transferred / total allowance)).

  It cannot be negative, so is set to zero if the above equation is negative.
*/

class AdjustedBroughtForwardAllowance extends BaseComponentClass{

  "Adjusted Value Being Transferred" should {
    "return the correctly calculated value" when {
      "Case 1" in {
        calculator.adjustedValueBeingTransferred(100000, 0, 50000) shouldBe 50000
      }

      "Case 2" in {
        calculator.adjustedValueBeingTransferred(100000, 0, 0) shouldBe 0
      }

      "Case 3" in {
        calculator.adjustedValueBeingTransferred(100000, 50000, 30000) shouldBe 15000
      }

      "Case 4" in {
        calculator.adjustedValueBeingTransferred(100000, 50000, 0) shouldBe 0
      }

      "Case 5" in {
        calculator.adjustedValueBeingTransferred(100000, 100000, 30000) shouldBe 0
      }

      "Case 6" in {
        calculator.adjustedValueBeingTransferred(100000, 200000, 30000) shouldBe 0
      }
    }
  }
}
