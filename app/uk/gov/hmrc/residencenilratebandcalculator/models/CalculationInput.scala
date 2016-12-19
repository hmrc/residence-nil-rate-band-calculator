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
import play.api.libs.json._

import scala.util.{Failure, Success, Try}

case class CalculationInput(dateOfDeath: LocalDate,
                            grossEstateValue: Int,
                            propertyValue: Int,
                            chargeableTransferAmount: Int,
                            percentageCloselyInherited: Int,
                            broughtForwardAllowance: Option[Int] = None) {
  require(grossEstateValue >= 0, """{"grossEstateValue" : "error.expected.number.non_negative"}""")
  require(propertyValue >= 0, """{"propertyValue" : "error.expected.number.non_negative"}""")
  require(percentageCloselyInherited >= 0, """{"percentageCloselyInherited" : "error.expected.number.non_negative"}""")
  require(percentageCloselyInherited <= 100, """{"percentageCloselyInherited" : "error.expected.number.100_at_most"}""")
  require(broughtForwardAllowance.getOrElse(0) >= 0, """{"broughtForwardAllowance" : "error.expected.number.non_negative"}""")
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
