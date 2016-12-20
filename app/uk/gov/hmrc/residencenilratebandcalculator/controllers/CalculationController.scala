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

package uk.gov.hmrc.residencenilratebandcalculator.controllers

import javax.inject.Inject

import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json._
import play.api.mvc.{Action, BodyParsers, Controller}
import uk.gov.hmrc.residencenilratebandcalculator.converters.HttpErrorResponse
import uk.gov.hmrc.residencenilratebandcalculator.converters.Percentify._
import uk.gov.hmrc.residencenilratebandcalculator.models.{CalculationInput, Calculator}

import scala.concurrent.Future

class CalculationController @Inject()(calculator: Calculator, val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def calculate() = Action.async(BodyParsers.parse.json) {
    implicit request => {
      CalculationInput(request.body) match {
        case Right(input) =>
          Future.successful(Ok(Json.toJson(calculator(input).get)))
        case Left(errors) =>
          Future.successful(BadRequest(HttpErrorResponse(BAD_REQUEST, "error.json_parsing_failure", errors)))
      }
    }
  }
}
