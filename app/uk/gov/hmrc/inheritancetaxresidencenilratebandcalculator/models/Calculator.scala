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
import uk.gov.hmrc.inheritancetaxresidencenilratebandcalculator.converters.Percentify._

/* Terms of art:
 * Percentage closely inherited = The percentage of a property passing to a direct descendant
 */

object Calculator {

  val taperRate = 2

  def apply(dateOfDeath: LocalDate,
            estateValue: Int,
            propertyValue: Int,
            percentageCloselyInherited: Percent,
            percentageBroughtForwardAllowance: Percent = 0 percent): Either[(String, String), CalculationResult] = {

    if (estateValue < 0) {
      Left(("INVALID_INPUTS", "The estate value must be greater or equal to zero."))
    } else if (propertyValue < 0) {
      Left(("INVALID_INPUTS", "The property value must be greater or equal to zero."))
    } else if (percentageBroughtForwardAllowance < 0.percent) {
      Left(("INVALID_INPUTS", "The brought forward allowance percentage must be greater or equal to zero."))
    } else if (percentageCloselyInherited < 0.percent || percentageCloselyInherited > 100.percent) {
      Left(("INVALID_INPUTS", "The percentage closely inherited must be between zero and one hundred."))
    } else {
      val totalAllowance = increaseByPercentage(ResidenceNilRateBand(dateOfDeath), percentageBroughtForwardAllowance)
      val amountToTaper = math.max(estateValue - TaperBand(dateOfDeath), 0) / taperRate
      val taperedAllowance = math.max(totalAllowance - amountToTaper, 0)

      val propertyCloselyInherited = percentageCloselyInherited.asDecimal * propertyValue toInt

      val residenceNilRateAmount = math.min(propertyCloselyInherited, taperedAllowance)
      val carryForwardAmount = taperedAllowance - residenceNilRateAmount
      Right(CalculationResult(residenceNilRateAmount, carryForwardAmount))
    }
  }

  private def increaseByPercentage(amount: Int, percentage: Percent) = (1 + percentage.asDecimal) * amount toInt

}
