/*
 * Copyright 2021 HM Revenue & Customs
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
import org.joda.time.LocalDate
import play.api.{Environment, Logging}
import play.api.libs.json._
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController
import uk.gov.hmrc.residencenilratebandcalculator.models.GetNilRateAmountFromFile

import scala.concurrent.Future
import scala.util.{Failure, Success, Try}

class BandController @Inject()(env: Environment,
                               cc: ControllerComponents) extends BackendController(cc) with Logging {

  lazy val residenceNilRateBand: GetNilRateAmountFromFile = {
    new GetNilRateAmountFromFile(env, "data/RNRB-amounts-by-year.json")
  }

  def getBand(dateStr: String): Action[AnyContent] = Action.async  {
    Try(LocalDate.parse(dateStr)) match {
      case Success(parsedDate) => residenceNilRateBand(parsedDate) match {
        case Success(band) => Future.successful(Ok(Json.toJson(band)))
        case Failure(e) =>
          logger.error("[BandController][getBand] : Unable to get band", e)
          Future.successful(BadRequest)
      }
      case Failure(e) =>
        logger.error(s"[BandController][getBand] : Unable to parse date $dateStr", e)
        Future.successful(BadRequest)
    }
  }
}
