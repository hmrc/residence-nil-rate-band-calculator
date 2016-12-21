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
import uk.gov.hmrc.residencenilratebandcalculator.models.{CalculationInput, Calculator}

import scala.concurrent.Future
import scala.util.{Failure, Success}

class CalculationController @Inject()(calculator: Calculator)(implicit val messagesApi: MessagesApi) extends Controller with I18nSupport {

  def calculate() = Action.async(BodyParsers.parse.json) {
    implicit request => {
      CalculationInput(request.body) match {
        case Right(input) =>
          calculator(input) match {
            case Success(result) => Future.successful(Ok(Json.toJson(result)))
            case Failure(error) => Future.successful(InternalServerError(HttpErrorResponse(INTERNAL_SERVER_ERROR, error.getMessage)))
          }
        case Left(jsonErrors) =>
          Future.successful(BadRequest(HttpErrorResponse(BAD_REQUEST, "error.json_parsing_failure", jsonErrors)))
      }
    }
  }
}
