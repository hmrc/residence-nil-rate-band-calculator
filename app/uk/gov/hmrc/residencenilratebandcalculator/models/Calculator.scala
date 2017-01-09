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

import javax.inject.{Inject, Singleton}

import org.joda.time.LocalDate
import play.api.Environment
import uk.gov.hmrc.residencenilratebandcalculator.converters.Percentify._

import scala.util.{Failure, Success, Try}

/* Terms of art:
 * Percentage closely inherited = The percentage of a property passing to a direct descendant
 */

@Singleton
class Calculator @Inject()(env: Environment) {

  lazy val residenceNilRateBand: GetNilRateAmountFromFile = {
    new GetNilRateAmountFromFile(env, "data/RNRB-amounts-by-year.json")
  }

  val taperRate = 2
  val invalidInputError = "INVALID_INPUTS"
  val lostRNRBEarliestDisposalDate = new LocalDate(2015, 7, 8)
  val legislativeStartDate = new LocalDate(2017, 4, 6)

  def lostRnrb(dateOfDeath: LocalDate,
               dateOfDisposalOfFormerProperty: LocalDate,
               valueOfFormerProperty: Int,
               valueOfTransferredRnrb: Int,
               valueOfFinalProperty: Int): Try[Int] = {

    if (valueOfFormerProperty < 0) {
      Failure(new RuntimeException(s"$invalidInputError: The former property value must be greater or equal to zero."))
    } else if (valueOfTransferredRnrb < 0) {
      Failure(new RuntimeException(s"$invalidInputError: The transferred RNRB value must be greater or equal to zero."))
    } else if (valueOfFinalProperty < 0) {
      Failure(new RuntimeException(s"$invalidInputError: The percentage of final property must be greater or equal to zero."))
    } else if (dateOfDisposalOfFormerProperty.isBefore(lostRNRBEarliestDisposalDate)) {
      Success(0)
    } else {

      for {
        rnrbOnDeath <- residenceNilRateBand(dateOfDeath)
        rnrbWhenFormerPropertyDisposedOf <- residenceNilRateBand(dateOfDisposalOfFormerProperty)
      } yield {
        val rnrbOnDeathWithTransferredRnrb = rnrbOnDeath + valueOfTransferredRnrb
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
        val percentageOfRnrbOfFinalProperty = fractionAsBoundedPercent(valueOfFinalProperty.toDouble / rnrbOnDeath)

        val maxRnrbOnSaleOfFormerProperty = rnrbWhenFormerPropertyDisposedOf + valueOfTransferredRnrb
        val percentageOfRnrbOfSale = fractionAsBoundedPercent(valueOfFormerProperty / maxRnrbOnSaleOfFormerProperty.toDouble)
        val finalPercentage = percentageOfRnrbOfSale - percentageOfRnrbOfFinalProperty

        finalPercentage * rnrbOnDeathWithTransferredRnrb toInt
      }
    }
  }

  def personsFormerAllowance(dateOfDisposal: LocalDate,
                             rnrbAtDisposal: Int,
                             broughtForwardAllowanceAtDisposal: Int,
                             adjustedBroughtForwardAllowance: Int): Int = {
    require(broughtForwardAllowanceAtDisposal >= 0, "broughtForwardAllowanceAtDisposal cannot be negative")
    require(adjustedBroughtForwardAllowance >= 0, "adjustedBroughtForwardAllowance cannot be negative")
    require(rnrbAtDisposal >= 0, "rnrbAtDisposal cannot be negative")

    val availableBroughtForwardAllowance = if (dateOfDisposal.isBefore(legislativeStartDate)) 0 else broughtForwardAllowanceAtDisposal
    val excessBroughtForwardAllowance = math.max(adjustedBroughtForwardAllowance - availableBroughtForwardAllowance, 0)

    rnrbAtDisposal + availableBroughtForwardAllowance + excessBroughtForwardAllowance
  }

  def adjustedBroughtForwardAllowance(totalAllowance: Int,
                                      amountToTaper: Int,
                                      broughtForwardAllowance: Int): Int = {
    require(totalAllowance >= 0, "totalAllowance cannot be negative")
    require(amountToTaper >= 0, "amountToTaper cannot be negative")
    require(broughtForwardAllowance >= 0, "broughtForwardAllowance cannot be negative")

    math.max(broughtForwardAllowance - (amountToTaper.toDouble * (broughtForwardAllowance.toDouble / totalAllowance)), 0.0) toInt
  }

  def lostRelievableAmount(valueOfDisposedProperty: Int,
                           formerAllowance: Int,
                           chargeablePropertyValue: Int,
                           taperedAllowance: Int): Int = {
    require(valueOfDisposedProperty >= 0, "valueOfDisposedProperty cannot be negative")
    require(formerAllowance > 0, "formerAllowance must be greater than zero")
    require(chargeablePropertyValue >= 0, "chargeablePropertyValue cannot be negative")
    require(taperedAllowance >= 0, "taperedAllowance cannot be negative")

    if (taperedAllowance == 0) {
      0
    }
    else {

      val percentageOfFormerAllowance = fractionAsBoundedPercent(valueOfDisposedProperty.toDouble / formerAllowance)
      val percentageOfAllowanceOnDeath = fractionAsBoundedPercent(chargeablePropertyValue.toDouble / taperedAllowance)
      val difference = boundedPercentageDifferenceAsDouble(percentageOfFormerAllowance,percentageOfAllowanceOnDeath)

      println("*********************************************")
      println("Lost Relievable Amount")
      println("----------------------")
      println("Value of disposed property = " + valueOfDisposedProperty)
      println("Former allowance = " + formerAllowance)
      println("Property value closely inherited = " + chargeablePropertyValue)
      println("Tapered allowance = " + taperedAllowance)
      println("Percentage of former allowance = " + percentageOfFormerAllowance)
      println("Percentage of allowance on death = " + percentageOfAllowanceOnDeath)
      println("Difference = " + difference)
      val x = (difference * taperedAllowance.toDouble).toInt
      println("Result will be: " + x)

      (difference * taperedAllowance.toDouble) toInt
    }
  }

  def downsizingAllowance(downsizingDetails: DownsizingDetails,
                          rnrbAtDisposal: Int,
                          totalAllowance: Int,
                          amountToTaper: Int,
                          broughtForwardAllowance: Int,
                          propertyValue: Int): Int = {

    val adjustedBroughtForward = adjustedBroughtForwardAllowance(totalAllowance, amountToTaper, broughtForwardAllowance)
    val formerAllowance = personsFormerAllowance(downsizingDetails.dateOfDisposal, rnrbAtDisposal, downsizingDetails.broughtForwardAllowanceAtDisposal, adjustedBroughtForward)
    val taperedAllowance = getTaperedAllowance(totalAllowance, amountToTaper)
    val lostAmount = lostRelievableAmount(downsizingDetails.valueOfDisposedProperty, formerAllowance, propertyValue, taperedAllowance)


    println("*********************************************")
    println("Downsizing Allowance")
    println("--------------------")
    println("Adjusted BFA = " + adjustedBroughtForward)
    println("Former allowance = " + formerAllowance)
    println("Tapered allowance = " + taperedAllowance)
    println("Lost amount = " + lostAmount)
    println("Property value = " + propertyValue)
    println("Output will be: " + math.min(downsizingDetails.valueCloselyInherited, lostAmount))

    math.min(downsizingDetails.valueCloselyInherited, lostAmount)
  }

  def apply(input: CalculationInput): Try[CalculationResult] = {

    for {
      rnrbOnDeath <- residenceNilRateBand(input.dateOfDeath)
      rnrbAtDisposal <- input.downsizingDetails match {
        case Some(details) => residenceNilRateBand(details.dateOfDisposal)
        case None => Success(0)
      }
    } yield {
      val totalAllowance = rnrbOnDeath + input.broughtForwardAllowance
      val amountToTaper = math.max(input.grossEstateValue - TaperBand(input.dateOfDeath), 0) / taperRate
      val adjustedAllowance = getTaperedAllowance(totalAllowance, amountToTaper)

      val propertyCloselyInherited = input.propertyValueAfterExemption match {
        case Some(values) => values.valueCloselyInherited
        case None => (input.percentageCloselyInherited percent) * input.propertyValue toInt
      }

      val propertyValueToConsider = input.propertyValueAfterExemption match {
        case Some(values) => values.value
        case None => input.propertyValue
      }

      val downsizingAddition: Int = input.downsizingDetails match {
        case Some(details) => downsizingAllowance(details, rnrbAtDisposal, totalAllowance, amountToTaper, input.broughtForwardAllowance, propertyValueToConsider)
        case None => 0
      }

      val residenceNilRateAmount = math.min(propertyCloselyInherited + downsizingAddition, adjustedAllowance)
      val carryForwardAmount = adjustedAllowance - residenceNilRateAmount


      println("*********************************************")
      println("*********************************************")
      println("*********************************************")
      println("Property closely inherited = " + propertyCloselyInherited)
      println("Downsizing addition = " + downsizingAddition)
      println("Residence nil rate amount = " + residenceNilRateAmount)
      println("Carry forward amount = " + carryForwardAmount)
      println("*********************************************")
      println("*********************************************")
      println("*********************************************")


      CalculationResult(residenceNilRateAmount, rnrbOnDeath, carryForwardAmount)
    }
  }

  private def getTaperedAllowance(totalAllowance: Int, amountToTaper: Int) = math.max(totalAllowance - amountToTaper, 0)

  private def fractionAsBoundedPercent(v: Double) = math.min(v * 100, 100) percent

  private def boundedPercentageDifferenceAsDouble(a: Percent, b: Percent) = math.max((a - b) asDecimal, 0.0)
}
