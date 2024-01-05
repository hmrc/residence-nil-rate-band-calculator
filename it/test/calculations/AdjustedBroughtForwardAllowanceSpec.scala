/*
 * Copyright 2024 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package calculations

import helpers.BaseComponentClass

/*
  Tests of the calculation for adjusted Value Being Transferred.

  Adjusted Value Being Transferred is calculated as:
  Value Being Transferred - (amount to taper * (Value Being Transferred / total allowance)).

  It cannot be negative, so is set to zero if the above equation is negative.
*/

class AdjustedBroughtForwardAllowanceSpec extends BaseComponentClass{

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
