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

package steps

import cucumber.api.scala.{EN, ScalaDsl}
import org.joda.time.LocalDate
import org.scalatest.Matchers
import org.scalatest.mock.MockitoSugar
import play.api.Environment
import uk.gov.hmrc.residencenilratebandcalculator.models.Calculator

class CalculatorSteps extends ScalaDsl with EN with Matchers with MockitoSugar {

  When("""^I calculate adjusted brought forwards allowance with (\d*), (\d*) and (\d*)$""") {
    (totalAllowance: Int, amountToTaper: Int, broughtForwardAllowance: Int) =>
      val env = mock[Environment]
      var calculator = new Calculator(env)
      CalculationContext.adjustedBroughtForwardAllowance = calculator.adjustedBroughtForwardAllowance(totalAllowance, amountToTaper, broughtForwardAllowance)
  }

  When("""^I calculate persons former allowance with (.*), (\d*), (\d*) and (\d*)$""") {
    (dateOfDisposal: String, rnrbOnDisposal: Int, broughtForwardAllowance: Int, adjustedBroughtForwardAllowance: Int) =>
      val env = mock[Environment]
      var calculator = new Calculator(env)
      val parsedDate = LocalDate.parse(dateOfDisposal)
      CalculationContext.personsFormerAllowance = calculator.personsFormerAllowance(parsedDate, rnrbOnDisposal, broughtForwardAllowance, adjustedBroughtForwardAllowance)
  }

  Then("""^the adjusted brought forward allowance should be (\d*)$""") { (expectedAnswer: Int) =>
    CalculationContext.adjustedBroughtForwardAllowance shouldBe expectedAnswer
  }

  Then("""^the persons former allowance should be (\d*)$""") { (expectedAnswer: Int) =>
    CalculationContext.personsFormerAllowance shouldBe expectedAnswer
  }
}
