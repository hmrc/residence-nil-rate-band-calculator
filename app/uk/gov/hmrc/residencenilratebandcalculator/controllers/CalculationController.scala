/*
 * Copyright 2024 HM Revenue & Customs
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
import play.api.Logging
import play.api.i18n.I18nSupport
import play.api.libs.json._
import play.api.mvc.{Action, ControllerComponents, PlayBodyParsers}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import uk.gov.hmrc.residencenilratebandcalculator.converters.HttpErrorResponse
import uk.gov.hmrc.residencenilratebandcalculator.models.{CalculationInput, Calculator}

import scala.concurrent.Future
import scala.util.{Failure, Success}

class CalculationController @Inject() (calculator: Calculator)(cc: ControllerComponents, bodyParsers: PlayBodyParsers)
    extends BackendController(cc)
    with I18nSupport
    with Logging {

  def calculate(): Action[JsValue] = Action.async(bodyParsers.json) { implicit request =>
    CalculationInput(request.body) match {
      case Right(input) =>
        calculator(input) match {
          case Success(result) => Future.successful(Ok(Json.toJson(result)))
          case Failure(error) =>
            logger.error("[CalculationController][calculate] : Unable to calculate", error)
            Future.successful(InternalServerError(HttpErrorResponse(INTERNAL_SERVER_ERROR, error.getMessage)))
        }
      case Left(jsonErrors) =>
        logger.error("[CalculationController][calculate] : Failed to parse the provided JSON object")
        Future.successful(BadRequest(HttpErrorResponse(BAD_REQUEST, "error.json_parsing_failure", jsonErrors)))
    }
  }

}
