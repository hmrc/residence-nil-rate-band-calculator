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
        val input = CalculationInput(new LocalDate(2021, 1, 1), 490000, 490000, 300000, 100, 0)
        Calculator(input) shouldBe CalculationResult(175000, 0)
      }

      "give RNRA equal to the threshold for the 2019/20 tax year" in {
        val input = CalculationInput(new LocalDate(2020, 1, 1), 490000, 490000, 300000, 100, 0)
        Calculator(input) shouldBe CalculationResult(150000, 0)
      }

      "include brought forward RNRB in the RNRA and carry-forward amount results" in {
        val input = CalculationInput(new LocalDate(2021, 1, 1), 500000, 500000, 250000, 100, 175000)
        Calculator(input) shouldBe CalculationResult(250000, 100000)
      }
    }

    "all assets are left to direct descendants and leaving a property worth less than the RNRB threshold" must {

      "give RNRA equal to the property value and carry-forward amount equal to (threshold - property value)" in {
        val input = CalculationInput(new LocalDate(2020, 1, 1), 470000, 470000, 80000, 100, 0)
        Calculator(input) shouldBe CalculationResult(80000, 70000)
      }
    }

    "part of a property is left to direct descendants, and the percentage left is above the RNRB threshold" must {
      "give RNRA equal to the RNRB threshold" in {
        val input = CalculationInput(new LocalDate(2021, 1, 1), 500000, 500000, 400000, 50, 0)
        Calculator(input) shouldBe CalculationResult(175000, 0)
      }
    }

    "part of a property is left to direct descendants, and the percentage left is below the RNRB threshold" must {
      "give RNRA equal to the percentage of the property value" in {
        val input = CalculationInput(new LocalDate(2021, 1, 1), 500000, 500000, 200000, 50, 0)
        Calculator(input) shouldBe CalculationResult(100000, 75000)
      }
    }

    "the estate is above the tapering threshold and the property is worth more than the RNRB threshold" must {
      "give RNRA equal to the tapered away amount of the threshold" in {
        val input = CalculationInput(new LocalDate(2021, 1, 1), 2100000, 2100000, 500000, 100, 0)
        Calculator(input) shouldBe CalculationResult(125000, 0)
      }

      "give RNRA equal to the tapered away amount of the threshold in the 2018/19 tax year" in {
        val input = CalculationInput(new LocalDate(2018, 7, 1), 2100000, 2100000, 450000, 100, 0)
        Calculator(input) shouldBe CalculationResult(75000, 0)
      }
    }

    "the estate is above the tapering threshold and the property is worth less than the RNRB threshold" must {
      "give RNRA equal to the tapered away amount of the threshold" in {
        val input = CalculationInput(new LocalDate(2021, 1, 1), 2100000, 2100000, 100000, 100, 0)
        Calculator(input) shouldBe CalculationResult(100000, 25000)
      }
    }

    "calculating lost RNRB" must {
      "calculate the correct lost RNRB when there is transferred RNRB, a former home that was sold, and no final home (case study 12)" in {
        Calculator.lostRnrb(new LocalDate(2020, 11, 1), new LocalDate(2018, 6, 14), 195000, 175000, 0) shouldBe Right(227500)
      }

      "calculate the lost RNRB as zero  if the date of disposal of the former property is before 8 July 2015  (date of sale before RNRB)" in {
        Calculator.lostRnrb(new LocalDate(2020, 11, 1), new LocalDate(2015, 7, 7), 195000, 175000, 0) shouldBe Right(0)
      }

      "calculate the lost RNRB as zero if the value of the property is greater than the available RNRB the percentage is limited to 100% (case study 13)" in {
        Calculator.lostRnrb(new LocalDate(2020, 1, 1), new LocalDate(2018, 8, 21), 450000, 0, 200000) shouldBe Right(0)
      }

      "calculate the lost RNRB as correctly if the value of the final property is less than the total amount of RNRB available (case study 14)" in {
        Calculator.lostRnrb(new LocalDate(2020, 9, 10), new LocalDate(2018, 8, 21), 500000, 0, 105000) shouldBe Right(70000)
      }

      "calculate the lost RNRB correctly if the former property was disposed after 8 July 2015 but before 6 April 2017, a 100000 RNRB Band applies (case study 15)" in {
        Calculator.lostRnrb(new LocalDate(2019, 12, 10), new LocalDate(2016, 3, 21), 400000, 0, 105000) shouldBe Right(45000)
      }

      "calculate the lost RNRB correctly if there is no home in the estate (case study 16) " in {
        Calculator.lostRnrb(new LocalDate(2021, 3, 3), new LocalDate(2018, 10, 5), 285000, 0, 0) shouldBe Right(175000)
      }

      "calculate the lost RNRB as correctly if the value of the final property is less than the total amount of RNRB available (case study 17)" in {
        Calculator.lostRnrb(new LocalDate(2021, 1, 10), new LocalDate(2019, 5, 21), 90000, 0, 0) shouldBe Right(105000)
      }

      "calculate the lost RNRB correctly when there is transferred RNRB (case study 18)" in {
        Calculator.lostRnrb(new LocalDate(2021, 3, 27), new LocalDate(2018, 10, 3), 285000, 175000, 0) shouldBe Right(332500)
      }

      "calculate the lost RNRB correctly when there is transferred RNRB and a final property" in {
        pending
        // Note that this test depends on the answer given to the long question asked in the comment in lostRNRB. If the
        // alternative form of teh calculation is used, this test should be added.
        Calculator.lostRnrb(new LocalDate(2021, 3, 27), new LocalDate(2018, 10, 3), 285000, 175000, 90000) shouldBe Right(242500)
      }

      "given an error when the value of the former property is less than zero" in {
        Calculator.lostRnrb(new LocalDate(2021, 1, 1), new LocalDate(2019, 1, 1), -1, 0, 0) shouldBe
          Left(("INVALID_INPUTS", "The former property value must be greater or equal to zero."))
      }

      "given an error when the value of transferred RNRB is less than zero" in {
        Calculator.lostRnrb(new LocalDate(2021, 1, 1), new LocalDate(2019, 1, 1), 100000, -1, 0) shouldBe
          Left(("INVALID_INPUTS", "The transferred RNRB value must be greater or equal to zero."))
      }

      "given an error when the value of the final property is less than zero" in {
        Calculator.lostRnrb(new LocalDate(2021, 1, 1), new LocalDate(2019, 1, 1), 100000, 0, -1) shouldBe
          Left(("INVALID_INPUTS", "The value of the final property must be greater or equal to zero."))
      }
    }
  }
}
