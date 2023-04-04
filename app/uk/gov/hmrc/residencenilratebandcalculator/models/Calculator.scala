/*
 * Copyright 2023 HM Revenue & Customs
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

import scala.util.{Success, Try}

@Singleton
class Calculator @Inject()(env: Environment) {

  lazy val residenceNilRateBand: GetNilRateAmountFromFile = {
    new GetNilRateAmountFromFile(env, "data/RNRB-amounts-by-year.json")
  }

  lazy val taperBand: GetTaperBandFromFile = {
    new GetTaperBandFromFile(env, "data/Taper-bands-by-year.json")
  }

  val taperRate = 2
  val earliestDisposalDate = new LocalDate(2015, 7, 8)
  val legislativeStartDate = new LocalDate(2017, 4, 6)


  def personsFormerAllowance(datePropertyWasChanged: LocalDate,
                             rnrbOnPropertyChange: Int,
                             valueAvailableWhenPropertyChanged: Int,
                             adjustedValueBeingTransferred: Int): Int = {
    require(valueAvailableWhenPropertyChanged >= 0, "valueAvailableWhenPropertyChanged cannot be negative")
    require(adjustedValueBeingTransferred >= 0, "adjustedValueBeingTransferred cannot be negative")
    require(rnrbOnPropertyChange >= 0, "rnrnOnPropertyChange cannot be negative")

    val availableValueBeingTransferred = if (datePropertyWasChanged.isBefore(legislativeStartDate)) 0 else valueAvailableWhenPropertyChanged
    val excessValueBeingTransferred = math.max(adjustedValueBeingTransferred - availableValueBeingTransferred, 0)

    rnrbOnPropertyChange + availableValueBeingTransferred + excessValueBeingTransferred
  }

  def adjustedValueBeingTransferred(totalAllowance: Int,
                                      amountToTaper: Int,
                                      valueBeingTransferred: Int): Int = {
    require(totalAllowance >= 0, "totalAllowance cannot be negative")
    require(amountToTaper >= 0, "amountToTaper cannot be negative")
    require(valueBeingTransferred >= 0, "valueBeingTransferred cannot be negative")

    math.max(valueBeingTransferred - (amountToTaper.toDouble * (valueBeingTransferred.toDouble / totalAllowance)), 0.0) toInt
  }

  def lostRelievableAmount(valueOfChangedProperty: Int,
                           formerAllowance: Int,
                           propertyValue: Int,
                           taperedAllowance: Int): Int = {
    require(valueOfChangedProperty >= 0, "valueOfChangedProperty cannot be negative")
    require(formerAllowance > 0, "formerAllowance must be greater than zero")
    require(propertyValue >= 0, "propertyValue cannot be negative")
    require(taperedAllowance >= 0, "taperedAllowance cannot be negative")

    taperedAllowance match {
      case 0 => 0
      case _ => {
        val percentageOfFormerAllowance = fractionAsBoundedPercent(valueOfChangedProperty.toDouble / formerAllowance)
        val percentageOfAllowanceOnDeath = fractionAsBoundedPercent(propertyValue.toDouble / taperedAllowance)
        val difference = boundedPercentageDifferenceAsDouble(percentageOfFormerAllowance,percentageOfAllowanceOnDeath)

        math.rint(difference * taperedAllowance.toDouble) toInt
      }
    }
  }

  def downsizingAllowance(downsizingDetails: Option[DownsizingDetails],
                          rnrnOnPropertyChange: Int,
                          totalAllowance: Int,
                          amountToTaper: Int,
                          valueBeingTransferred: Int,
                          propertyValue: Int): Int = {

    downsizingDetails match {
      case None => 0
      case Some(details) if details.datePropertyWasChanged isBefore earliestDisposalDate => 0
      case Some(details) =>
        val adjustedBroughtForward = adjustedValueBeingTransferred(totalAllowance, amountToTaper, valueBeingTransferred)
        val formerAllowance = personsFormerAllowance(details.datePropertyWasChanged, rnrnOnPropertyChange, details.valueAvailableWhenPropertyChanged, adjustedBroughtForward)
        val adjustedAllowance = taperedAllowance(totalAllowance, amountToTaper)
        val lostAmount = lostRelievableAmount(details.valueOfChangedProperty, formerAllowance, propertyValue, adjustedAllowance)

        math.min(details.valueOfAssetsPassing, lostAmount)
    }
  }

  def apply(input: CalculationInput): Try[CalculationResult] = {

    for {
      rnrbOnDeath <- residenceNilRateBand(input.dateOfDeath)
      rnrbOnPropertyChange <- input.downsizingDetails match {
        case Some(details) => residenceNilRateBand(details.datePropertyWasChanged)
        case None => Success(0)
      }
      taperBandOnDeath <- taperBand(input.dateOfDeath)
    } yield {
      val defaultAllowance = rnrbOnDeath + input.valueBeingTransferred
      val amountToTaper = math.max(input.valueOfEstate - taperBandOnDeath.threshold, 0) / taperBandOnDeath.rate
      val adjustedAllowance = taperedAllowance(defaultAllowance, amountToTaper)

      val downsizingAddition = downsizingAllowance(input.downsizingDetails, rnrbOnPropertyChange, defaultAllowance, amountToTaper, input.valueBeingTransferred, input.propertyValue)

      val residenceNilRateAmount = math.min(input.propertyValuePassedToDirectDescendants + downsizingAddition, adjustedAllowance)
      val carryForwardAmount = adjustedAllowance - residenceNilRateAmount

      CalculationResult(residenceNilRateAmount, rnrbOnDeath, carryForwardAmount, defaultAllowance, adjustedAllowance)
    }
  }

  private def taperedAllowance(totalAllowance: Int, amountToTaper: Int) = math.max(totalAllowance - amountToTaper, 0)

  private def fractionAsBoundedPercent(v: Double) = math.min(v * 100, 100) percent

  private def boundedPercentageDifferenceAsDouble(a: Percent, b: Percent) = math.max((a - b) asDecimal, 0.0)
}
