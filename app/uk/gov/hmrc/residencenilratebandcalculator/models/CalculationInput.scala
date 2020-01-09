/*
 * Copyright 2020 HM Revenue & Customs
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
import play.api.libs.json._
import play.api.libs.json.JodaReads._
import play.api.libs.json.JodaWrites._
import uk.gov.hmrc.residencenilratebandcalculator.converters.Percentify._

import scala.util.{Failure, Success, Try}

case class CalculationInput(dateOfDeath: LocalDate,
                            valueOfEstate: Int,
                            chargeableEstateValue: Int,
                            propertyValue: Int,
                            percentagePassedToDirectDescendants: BigDecimal,
                            valueBeingTransferred: Int,
                            propertyValueAfterExemption: Option[PropertyValueAfterExemption] = None,
                            downsizingDetails: Option[DownsizingDetails] = None) {
  require(valueOfEstate >= 0, """{"valueOfEstate" : "error.expected.number.non_negative"}""")
  require(propertyValue >= 0, """{"propertyValue" : "error.expected.number.non_negative"}""")
  require(percentagePassedToDirectDescendants >= 0, """{"percentagePassedToDirectDescendants" : "error.expected.number.non_negative"}""")
  require(percentagePassedToDirectDescendants <= 100, """{"percentagePassedToDirectDescendants" : "error.expected.number.100_at_most"}""")
  require(valueBeingTransferred >= 0, """{"valueBeingTransferred" : "error.expected.number.non_negative"}""")

  def propertyValuePassedToDirectDescendants = propertyValueAfterExemption match {
    case Some(values) => values.inheritedValue
    case None => (percentagePassedToDirectDescendants percent) * propertyValue toInt
  }
}

object CalculationInput {
  implicit val formats: Format[CalculationInput] = Json.format[CalculationInput]

  private def extractErrors(errors: JsValue): Seq[(String, String)] = {
    errors.as[JsObject].fields.map {
      error => (error._1.stripPrefix("obj."), (error._2 \ 0 \ "msg" \ 0).as[String])
    }
  }

  private def extractErrors(throwable: Throwable): Seq[(String, String)] = {
    Json.parse(throwable.getMessage.stripPrefix("requirement failed: ")).as[JsObject].fields.map {
      error => (error._1, error._2.as[String])
    }
  }

  def apply(json: JsValue): Either[Seq[(String, String)], CalculationInput] = {
    Try(Json.fromJson[CalculationInput](json)) match {
      case Success(calculationInput) => calculationInput match {
        case JsSuccess(input, _) => Right(input)
        case errors: JsError => Left(extractErrors(JsError.toJson(errors)))
      }
      case Failure(ex) => Left(extractErrors(ex))
    }
  }
}

case class PropertyValueAfterExemption(value: Int, inheritedValue: Int) {
  require(value >= 0, """{"value" : "error.expected.number.non_negative"}""")
  require(inheritedValue >= 0, """{"inheritedValue" : "error.expected.number.non_negative"}""")
}

object PropertyValueAfterExemption {
  implicit val formats: OFormat[PropertyValueAfterExemption] = Json.format[PropertyValueAfterExemption]
}

case class DownsizingDetails(datePropertyWasChanged: LocalDate,
                             valueOfChangedProperty: Int,
                             valueOfAssetsPassing: Int,
                             valueAvailableWhenPropertyChanged: Int) {
  require(valueOfChangedProperty >= 0, """{"valueOfChangedProperty" : "error.expected.number.non_negative"}""")
  require(valueOfAssetsPassing >= 0, """{"valueOfAssetsPassing" : "error.expected.number.non_negative"}""")
  require(valueAvailableWhenPropertyChanged >= 0, """{"valueAvailableWhenPropertyChanged" : "error.expected.number.non_negative"}""")
}

object DownsizingDetails {
  implicit val formats: OFormat[DownsizingDetails] = Json.format[DownsizingDetails]
}
