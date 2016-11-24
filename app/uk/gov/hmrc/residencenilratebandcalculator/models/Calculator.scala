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
import uk.gov.hmrc.residencenilratebandcalculator.converters.Percentify._

/* Terms of art:
 * Percentage closely inherited = The percentage of a property passing to a direct descendant
 */

object Calculator {

  val taperRate = 2
  val invalidInputError = "INVALID_INPUTS"
  val lostRNRBEarliestDisposalDate = new LocalDate(2015, 7, 8)

  def lostRNRB(dateOfDeath: LocalDate,
               dateOfDisposalOfFormerProperty: LocalDate,
               valueOfFormerProperty: Int,
               valueOfTransferredRNRB: Int,
               valueOfFinalProperty: Int): Either[(String, String), Int] = {

    if (valueOfFormerProperty < 0) {
      Left((invalidInputError, "The former property value must be greater or equal to zero."))
    } else if (valueOfTransferredRNRB < 0) {
      Left((invalidInputError, "The transferred RNRB value must be greater or equal to zero."))
    } else if (valueOfFinalProperty < 0) {
      Left((invalidInputError, "The percentage of final property must be between zero and one hundred."))
    }  else if (dateOfDisposalOfFormerProperty.isBefore(lostRNRBEarliestDisposalDate)) {
      Right(0)
    } else {
      val RNRBOnDeath = ResidenceNilRateBand(dateOfDeath)
      val RNRBOnDeathWithTransferredRNRB = RNRBOnDeath + valueOfTransferredRNRB

      // Note that the following calculation is slightly ambiguous:
      //  this is step 3 in case studies 12, 13, 14, 15. The case studies always show that the calculation of
      //  maximum available RNRB of the _final_ property depends on the RNRB in effect at time of death. These case
      //  studies also set the transferred RNRB to zero. In the cases studies where there is transferred RNRB
      //  there is no final property in the estate at time of death.
      //
      //   Therefore calculating this percentage based on the RNRB on death gives the same result for the case studies
      //   as whether or not any transferred RNRB is to be taken into account. If it was taken into account this line
      //   would read:
      //
      //     val percentageOfRNRBOfFinalProperty = math.min((valueOfFinalProperty.toDouble / RNRBOnDeathWithTransferredRNRB) * 100, 100)
      //
      //  There is an outstanding question put to the business to clarify this.
      val percentageOfRNRBOfFinalProperty = math.min((valueOfFinalProperty.toDouble / RNRBOnDeath) * 100, 100)

      val maxRNRBOnSaleOfFormerProperty = ResidenceNilRateBand(dateOfDisposalOfFormerProperty) + valueOfTransferredRNRB
      val percentageOfRNRBOfSale = math.min((valueOfFormerProperty / maxRNRBOnSaleOfFormerProperty.toDouble) * 100, 100)
      val finalPercentage = percentageOfRNRBOfSale - percentageOfRNRBOfFinalProperty

      Right(RNRBOnDeathWithTransferredRNRB * (finalPercentage / 100) toInt)
    }
  }

  def apply(dateOfDeath: LocalDate,
            estateValue: Int,
            propertyValue: Int,
            percentageCloselyInherited: Percent,
            percentageBroughtForwardAllowance: Percent = 0 percent): Either[(String, String), CalculationResult] = {

    if (estateValue < 0) {
      Left((invalidInputError, "The estate value must be greater or equal to zero."))
    } else if (propertyValue < 0) {
      Left((invalidInputError, "The property value must be greater or equal to zero."))
    } else if (percentageBroughtForwardAllowance < 0.percent) {
      Left((invalidInputError, "The brought forward allowance percentage must be greater or equal to zero."))
    } else if (percentageCloselyInherited < 0.percent || percentageCloselyInherited > 100.percent) {
      Left((invalidInputError, "The percentage closely inherited must be between zero and one hundred."))
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
