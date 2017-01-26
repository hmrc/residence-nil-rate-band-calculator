/*
 * Copyright 2017 HM Revenue & Customs
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

import java.io.ByteArrayInputStream

import org.joda.time.LocalDate
import org.mockito.Matchers._
import org.mockito.Mockito.when
import org.scalatest.mock.MockitoSugar
import play.api.Environment
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}

import scala.util.Success

class CalculatorTest extends UnitSpec with WithFakeApplication with MockitoSugar {

  val env = mock[Environment]
  when(env.resourceAsStream(matches("data/RNRB-amounts-by-year.json"))) thenReturn Some(new ByteArrayInputStream(
    "{ \"2017-04-06\": 100000,  \"2018-04-06\": 125000,  \"2019-04-06\": 150000,  \"2020-04-06\": 175000}".getBytes))

  when(env.resourceAsStream(matches("data/Taper-bands-by-year.json"))) thenReturn Some(new ByteArrayInputStream(
    """{ "2017-04-06": {"threshold": 2000000, "rate": 2}}""".getBytes))

  val calculator = new Calculator(env)

  "Calculator" when {

    "all assets are left to direct descendants and leaving a property worth more than the RNRB threshold" must {

      "give RNRA equal to the threshold for the 2020/21 tax year" in {
        val input = CalculationInput(new LocalDate(2021, 1, 1), 490000, 490000, 300000, 100, 0)
        calculator(input) shouldBe Success(CalculationResult(175000, 175000, 0, 175000))
      }

      "give RNRA equal to the threshold for the 2019/20 tax year" in {
        val input = CalculationInput(new LocalDate(2020, 1, 1), 490000, 490000, 300000, 100, 0)
        calculator(input) shouldBe Success(CalculationResult(150000, 150000, 0, 150000))
      }

      "include brought forward RNRB in the RNRA and carry-forward amount results" in {
        val input = CalculationInput(new LocalDate(2021, 1, 1), 500000, 500000, 250000, 100, 175000)
        calculator(input) shouldBe Success(CalculationResult(250000, 175000, 100000, 350000))
      }
    }

    "all assets are left to direct descendants and leaving a property worth less than the RNRB threshold" must {

      "give RNRA equal to the property value and carry-forward amount equal to (threshold - property value)" in {
        val input = CalculationInput(new LocalDate(2020, 1, 1), 470000, 470000, 80000, 100, 0)
        calculator(input) shouldBe Success(CalculationResult(80000, 150000, 70000, 150000))
      }
    }

    "part of a property is left to direct descendants, and the percentage left is above the RNRB threshold" must {
      "give RNRA equal to the RNRB threshold" in {
        val input = CalculationInput(new LocalDate(2021, 1, 1), 500000, 500000, 400000, 50, 0)
        calculator(input) shouldBe Success(CalculationResult(175000, 175000, 0, 175000))
      }
    }

    "part of a property is left to direct descendants, and the percentage left is below the RNRB threshold" must {
      "give RNRA equal to the percentage of the property value" in {
        val input = CalculationInput(new LocalDate(2021, 1, 1), 500000, 500000, 200000, 50, 0)
        calculator(input) shouldBe Success(CalculationResult(100000, 175000, 75000, 175000))
      }
    }

    "the estate is above the tapering threshold and the property is worth more than the RNRB threshold" must {
      "give RNRA equal to the tapered away amount of the threshold" in {
        val input = CalculationInput(new LocalDate(2021, 1, 1), 2100000, 2100000, 500000, 100, 0)
        calculator(input) shouldBe Success(CalculationResult(125000, 175000, 0, 175000))
      }

      "give RNRA equal to the tapered away amount of the threshold in the 2018/19 tax year" in {
        val input = CalculationInput(new LocalDate(2018, 7, 1), 2100000, 2100000, 450000, 100, 0)
        calculator(input) shouldBe Success(CalculationResult(75000, 125000, 0, 125000))
      }
    }

    "the estate is above the tapering threshold and the property is worth less than the RNRB threshold" must {
      "give RNRA equal to the tapered away amount of the threshold" in {
        val input = CalculationInput(new LocalDate(2021, 1, 1), 2100000, 2100000, 100000, 100, 0)
        calculator(input) shouldBe Success(CalculationResult(100000, 175000, 25000, 175000))
      }
    }

    "part of the property is exempt from inheritance tax" must {
      "give RNRA using the value closely inherited after exemptions" in {
        val input = CalculationInput(new LocalDate(2021, 1, 1), 1000000, 1000000, 500000, 100, 0, Some(PropertyValueAfterExemption(100000, 100000)))
        calculator(input) shouldBe Success(CalculationResult(100000, 175000, 75000, 175000))
      }
    }
  }

  "calculating adjusted brought forward allowance" must {

    "give an error when total allowance is negative" in {
      val caught = intercept[IllegalArgumentException] {
        calculator.adjustedBroughtForwardAllowance(-1, 1, 1)
      }
      assert(caught.getMessage == "requirement failed: totalAllowance cannot be negative")
    }

    "give an error when amount to taper is negative" in {
      val caught = intercept[IllegalArgumentException] {
        calculator.adjustedBroughtForwardAllowance(1, -1, 1)
      }
      assert(caught.getMessage == "requirement failed: amountToTaper cannot be negative")
    }

    "give an error when brought forward allowance is negative" in {
      val caught = intercept[IllegalArgumentException] {
        calculator.adjustedBroughtForwardAllowance(1, 1, -1)
      }
      assert(caught.getMessage == "requirement failed: broughtForwardAllowance cannot be negative")
    }
  }

  "calculating persons former allowance" must {

    "ignore brought forward allowance at disposal when the date of disposal is before 6 April 2017" in {
      calculator.personsFormerAllowance(new LocalDate(2015, 4, 5), 100000, 50000, 0) shouldBe 100000
    }

    "give an error when RNRB at disposal is negative" in {
      val caught = intercept[IllegalArgumentException] {
        calculator.personsFormerAllowance(new LocalDate(), -1, 1, 1)
      }
      assert(caught.getMessage == "requirement failed: rnrbAtDisposal cannot be negative")
    }

    "give an error when brought forward allowance at disposal is negative" in {
      val caught = intercept[IllegalArgumentException] {
        calculator.personsFormerAllowance(new LocalDate(), 1, -1, 1)
      }
      assert(caught.getMessage == "requirement failed: broughtForwardAllowanceAtDisposal cannot be negative")
    }

    "give an error when adjusted brought forward allowance is negative" in {
      val caught = intercept[IllegalArgumentException] {
        calculator.personsFormerAllowance(new LocalDate(), 1, 1, -1)
      }
      assert(caught.getMessage == "requirement failed: adjustedBroughtForwardAllowance cannot be negative")
    }
  }

  "calculating lost relievable amount" must {

    "return 0 when the value of the disposed property is 0" in {
      calculator.lostRelievableAmount(0, 100000, 100000, 100000) shouldBe 0
    }

    "return 0 when the tapered allowance is 0" in {
      calculator.lostRelievableAmount(100000, 100000, 100000, 0) shouldBe 0
    }

    "return a value equal to the tapered allowance when the chargeable property value is 0 and the disposed property value is greater than or equal to the former allowance" in {
      calculator.lostRelievableAmount(200000, 100000, 0, 150000) shouldBe 150000
      calculator.lostRelievableAmount(200000, 200000, 0, 150000) shouldBe 150000
    }

    "return [tapered allowance] * [value of disposed property / former allowance] when the chargeable property value is 0 and the disposed property value is less than the former allowance" in {
      calculator.lostRelievableAmount(100000, 200000, 0, 150000) shouldBe 75000
    }

    "return 0 when the chargeable property value is equal to or greater than the tapered allowance" in {
      calculator.lostRelievableAmount(100000, 100000, 150000, 150000) shouldBe 0
      calculator.lostRelievableAmount(100000, 100000, 200000, 150000) shouldBe 0
    }

    "round correctly when given a values which result in a value of x.999999" in {
      // Due to imprecision in floating point number arithmetic, an issue was seen when the calculation "175000 * 0.7" was calculated as 122499.99999999
      // and then rounded down to 122499, instead of being rounded up as 122500
      calculator.lostRelievableAmount(400000, 125000, 52500, 175000) shouldBe 122500
    }

    "give an error when value of disposed property is negative" in {
      val caught = intercept[IllegalArgumentException] {
        calculator.lostRelievableAmount(-1, 1, 1, 1)
      }
      assert(caught.getMessage == "requirement failed: valueOfDisposedProperty cannot be negative")
    }

    "give an error when former allowance is negative" in {
      val caught = intercept[IllegalArgumentException] {
        calculator.lostRelievableAmount(1, -1, 1, 1)
      }
      assert(caught.getMessage == "requirement failed: formerAllowance must be greater than zero")
    }

    "give an error when former allowance is zero" in {
      val caught = intercept[IllegalArgumentException] {
        calculator.lostRelievableAmount(1, 0, 1, 1)
      }
      assert(caught.getMessage == "requirement failed: formerAllowance must be greater than zero")
    }

    "give an error when property value closely inherited is negative" in {
      val caught = intercept[IllegalArgumentException] {
        calculator.lostRelievableAmount(1, 1, -1, 1)
      }
      assert(caught.getMessage == "requirement failed: chargeablePropertyValue cannot be negative")
    }

    "give an error when tapered allowance is negative" in {
      val caught = intercept[IllegalArgumentException] {
        calculator.lostRelievableAmount(1, 1, 1, -1)
      }
      assert(caught.getMessage == "requirement failed: taperedAllowance cannot be negative")
    }
  }
}
