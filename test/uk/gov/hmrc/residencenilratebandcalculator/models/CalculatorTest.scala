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

package uk.gov.hmrc.residencenilratebandcalculator.models

import org.joda.time.LocalDate
import uk.gov.hmrc.play.test.UnitSpec
import uk.gov.hmrc.residencenilratebandcalculator.converters.Percentify._

class CalculatorTest extends UnitSpec {

  "Calculator" when {

    "all assets are left to direct descendants and leaving a property worth more than the RNRB threshold" must {

      "give RNRA equal to the threshold for the 2020/21 tax year" in {
        Calculator(new LocalDate(2021, 1, 1), 490000, 300000, 100 percent) shouldBe Right(CalculationResult(175000, 0))
      }

      "give RNRA equal to the threshold for the 2019/20 tax year" in {
        Calculator(new LocalDate(2020, 1, 1), 490000, 300000, 100 percent) shouldBe Right(CalculationResult(150000, 0))
      }

      "include brought forward RNRB in the RNRA and carry-forward amount results" in {
        Calculator(new LocalDate(2021, 1, 1), 500000, 250000, 100 percent, 100 percent) shouldBe Right(CalculationResult(250000, 100000))
      }
    }

    "all assets are left to direct descendants and leaving a property worth less than the RNRB threshold" must {

      "give RNRA equal to the property value and carry-forward amount equal to (threshold - property value)" in {
        Calculator(new LocalDate(2020, 1, 1), 470000, 80000, 100 percent) shouldBe Right(CalculationResult(80000, 70000))
      }
    }

    "part of a property is left to direct descendants, and the percentage left is above the RNRB threshold" must {
      "give RNRA equal to the RNRB threshold" in {
        Calculator(new LocalDate(2021, 1, 1), 500000, 400000, 50 percent) shouldBe Right(CalculationResult(175000, 0))
      }
    }

    "part of a property is left to direct descendants, and the percentage left is below the RNRB threshold" must {
      "give RNRA equal to the percentage of the property value" in {
        Calculator(new LocalDate(2021, 1, 1), 500000, 200000, 50 percent) shouldBe Right(CalculationResult(100000, 75000))
      }
    }

    "the estate is above the tapering threshold and the property is worth more than the RNRB threshold" must {
      "give RNRA equal to the tapered away amount of the threshold" in {
        Calculator(new LocalDate(2021, 1, 1), 2100000, 500000, 100 percent) shouldBe Right(CalculationResult(125000, 0))
      }

      "give RNRA equal to the tapered away amount of the threshold in the 2018/19 tax year" in {
        Calculator(new LocalDate(2018, 7, 1), 2100000, 450000, 100 percent) shouldBe Right(CalculationResult(75000, 0))
      }
    }

    "the estate is above the tapering threshold and the property is worth less than the RNRB threshold" must {
      "give RNRA equal to the tapered away amount of the threshold" in {
        Calculator(new LocalDate(2021, 1, 1), 2100000, 100000, 100 percent) shouldBe Right(CalculationResult(100000, 25000))
      }
    }
  }

  "Calculator" must {
    "give an error when supplied with a negative estate value" in {
      Calculator(new LocalDate(2021, 1, 1), -1, 300000, 0 percent).left.get shouldBe
        Tuple2("INVALID_INPUTS", "The estate value must be greater or equal to zero.")
    }

    "give an error when supplied with a negative property value" in {
      Calculator(new LocalDate(2021, 1, 1), 490000, -1, 0 percent).left.get shouldBe
        Tuple2("INVALID_INPUTS", "The property value must be greater or equal to zero.")
    }

    "give an error when supplied with a negative brought forward allowance percentage" in {
      Calculator(new LocalDate(2021, 1, 1), 490000, 300000, 0 percent, -0.1 percent).left.get shouldBe
        Tuple2("INVALID_INPUTS", "The brought forward allowance percentage must be greater or equal to zero.")
    }

    "give an error when supplied with a negative percentage closely inherited" in {
      Calculator(new LocalDate(2021, 1, 1), 490000, 300000, -0.01 percent, 12 percent).left.get shouldBe
        Tuple2("INVALID_INPUTS", "The percentage closely inherited must be between zero and one hundred.")
    }

    "give an error when supplied with a percentage closely inherited that is greater than 100%" in {
      Calculator(new LocalDate(2021, 1, 1), 490000, 300000, 100.01 percent, 12 percent).left.get shouldBe
        Tuple2("INVALID_INPUTS", "The percentage closely inherited must be between zero and one hundred.")
    }


  }
}
