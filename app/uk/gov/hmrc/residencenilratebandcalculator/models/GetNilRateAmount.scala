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
import play.api.Logger
import play.api.libs.json._
import uk.gov.hmrc.residencenilratebandcalculator.models.DateIntSortedMap._

import scala.collection.immutable.SortedMap
import scala.util.{Failure, Success, Try}

object GetNilRateAmount {

  private def getRateBand(date: LocalDate, rateBands: SortedMap[LocalDate, Int]): Option[(LocalDate, Int)] =
    rateBands.find {
      case (startDate: LocalDate, _) => (date equals startDate) || (date isAfter startDate)
    }

  def apply(date: LocalDate, rateBandAsJson: String): Try[Int] = {
    Try(Json.parse(rateBandAsJson)) match {
      case Success(json) =>
        Json.fromJson[SortedMap[LocalDate, Int]](json) match {
          case JsSuccess(rateBands, _) =>
            Success(getRateBand(date, rateBands) match {
              case Some((_, amount)) => amount
              case None => 0
            })
          case error: JsError => {
            Logger.error(s"Invalid rate band JSON:\n$rateBandAsJson")
            Failure(new RuntimeException(error.toString))
          }
        }
      case Failure(error) => {
        Logger.error(error.getMessage, error)
        Failure(error)
      }
    }
  }
}
