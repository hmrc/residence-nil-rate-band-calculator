/*
 * Copyright 2016 HM Revenue & Customs
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

package uk.gov.hmrc.inheritancetaxresidencenilratebandcalculator.models

import org.joda.time.LocalDate
import uk.gov.hmrc.play.test.UnitSpec

class CalculatorTest extends UnitSpec {

  "Calculator" when {

    "all assets are left to direct descendents and leaving a property worth more than the RNRB threshold" must {

      "give RNRA equal to the threshold for the 2020/21 tax year" in {
        Calculator(new LocalDate(2021, 1, 1), 490000, 300000) shouldBe Right(CalculationResult(175000, 0))
      }

      "give RNRA equal to the threshold for the 2019/20 tax year" in {
        Calculator(new LocalDate(2020, 1, 1), 490000, 300000) shouldBe Right(CalculationResult(150000, 0))
      }

      "include brought forward RNRB in the RNRA and CFA results" in {
        Calculator(new LocalDate(2021, 1, 1), 500000, 250000, 100) shouldBe Right(CalculationResult(250000, 100000))
      }
    }

    "all assets are left to direct descendents and leaving a property worth less than the RNRB threshold" must {

      "give RNRA equal to the property value and CFA equal to (threshold - property value)" in {
        Calculator(new LocalDate(2020, 1, 1), 470000, 80000) shouldBe Right(CalculationResult(80000, 70000))
      }
    }
  }

  "Calculator" must {
    // TODO: Check for negatives in all values, and check for percentages outside of the 0 - 100 bounds.
    "give an error when supplied with a negative estate value" in {
      Calculator(new LocalDate(2021, 1, 1), -490000, 300000).left.get shouldBe
        ("INVALID_INPUTS", "The estate value must be greater or equal to zero.")
    }

    "give an error when supplied with a negative property value" in {
      Calculator(new LocalDate(2021, 1, 1), 490000, -300000).left.get shouldBe
        ("INVALID_INPUTS", "The property value must be greater or equal to zero.")
    }

    "give an error when supplied with a negative brought forward allowance value" in {
      Calculator(new LocalDate(2021, 1, 1), 490000, 300000, -12).left.get shouldBe
        ("INVALID_INPUTS", "The brought forward allowance percentage must be greater or equal to zero.")
    }
  }
}
