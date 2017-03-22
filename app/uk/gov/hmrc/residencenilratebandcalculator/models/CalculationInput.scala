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

import org.joda.time.LocalDate
import play.api.libs.json._
import uk.gov.hmrc.residencenilratebandcalculator.converters.Percentify._

import scala.util.{Failure, Success, Try}

case class CalculationInput(dateOfDeath: LocalDate,
                            valueOfEstate: Int,
                            chargeableTransferAmount: Int,
                            propertyValue: Int,
                            percentageCloselyInherited: Int,
                            broughtForwardAllowance: Int,
                            propertyValueAfterExemption: Option[PropertyValueAfterExemption] = None,
                            downsizingDetails: Option[DownsizingDetails] = None) {
  require(valueOfEstate >= 0, """{"valueOfEstate" : "error.expected.number.non_negative"}""")
  require(propertyValue >= 0, """{"propertyValue" : "error.expected.number.non_negative"}""")
  require(percentageCloselyInherited >= 0, """{"percentageCloselyInherited" : "error.expected.number.non_negative"}""")
  require(percentageCloselyInherited <= 100, """{"percentageCloselyInherited" : "error.expected.number.100_at_most"}""")
  require(broughtForwardAllowance >= 0, """{"broughtForwardAllowance" : "error.expected.number.non_negative"}""")

  def propertyValueCloselyInherited = propertyValueAfterExemption match {
    case Some(values) => values.valueCloselyInherited
    case None => (percentageCloselyInherited percent) * propertyValue toInt
  }
}

object CalculationInput {
  implicit val formats: OFormat[CalculationInput] = Json.format[CalculationInput]

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

case class PropertyValueAfterExemption(value: Int, valueCloselyInherited: Int) {
  require(value >= 0, """{"value" : "error.expected.number.non_negative"}""")
  require(valueCloselyInherited >= 0, """{"valueCloselyInherited" : "error.expected.number.non_negative"}""")
}

object PropertyValueAfterExemption {
  implicit val formats: OFormat[PropertyValueAfterExemption] = Json.format[PropertyValueAfterExemption]
}

case class DownsizingDetails(dateOfDisposal: LocalDate,
                             valueOfDisposedProperty: Int,
                             valueCloselyInherited: Int,
                             broughtForwardAllowanceAtDisposal: Int) {
  require(valueOfDisposedProperty >= 0, """{"valueOfDisposedProperty" : "error.expected.number.non_negative"}""")
  require(valueCloselyInherited >= 0, """{"valueCloselyInherited" : "error.expected.number.non_negative"}""")
  require(broughtForwardAllowanceAtDisposal >= 0, """{"broughtForwardAllowanceAtDisposal" : "error.expected.number.non_negative"}""")
}

object DownsizingDetails {
  implicit val formats: OFormat[DownsizingDetails] = Json.format[DownsizingDetails]
}
