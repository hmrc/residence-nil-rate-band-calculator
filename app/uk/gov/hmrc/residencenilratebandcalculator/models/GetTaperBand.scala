/*
 * Copyright 2022 HM Revenue & Customs
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
import play.api.libs.json.{JsError, JsSuccess, Json}
import play.api.Logging
import uk.gov.hmrc.residencenilratebandcalculator.models.DateTaperBandSortedMap._

import scala.collection.immutable.SortedMap
import scala.util.{Failure, Success, Try}

object GetTaperBand extends Logging {

  private def getBand(date: LocalDate, bands: SortedMap[LocalDate, TaperBand]): Option[(LocalDate, TaperBand)] =
    bands.find {
      case (startDate: LocalDate, _) => (date equals startDate) || (date isAfter startDate)
    }

  def apply(date: LocalDate, bandAsJson: String): Try[TaperBand] = {
    Try(Json.parse(bandAsJson)) match {
      case Success(json) =>
        Json.fromJson[SortedMap[LocalDate, TaperBand]](json) match {
          case JsSuccess(bands, _) =>
            Success(getBand(date, bands) match {
              case Some((_, taperBand)) => taperBand
              case None => TaperBand(0, 1)
            })
          case error: JsError =>
            logger.error(s"Invalid taper band JSON:\n$bandAsJson")
            Failure(new RuntimeException(error.toString))
        }
      case Failure(error) =>
        logger.error(error.getMessage, error)
        Failure(error)
    }
  }
}
