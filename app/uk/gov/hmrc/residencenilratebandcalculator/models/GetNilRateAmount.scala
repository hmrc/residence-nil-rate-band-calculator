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
import uk.gov.hmrc.residencenilratebandcalculator.models.DateIntSortedMap._

import scala.collection.immutable.SortedMap
import scala.util.Try

object GetNilRateAmount {

  private def getRateBand(date: LocalDate, rateBands: SortedMap[LocalDate, Int]) =
    rateBands.find {
      case (startDate: LocalDate, _) => (date equals startDate) || (date isAfter startDate)
    }

  def apply(date: LocalDate, rateBandAsJson: String): Try[Int] = {
    Try(Json.fromJson[SortedMap[LocalDate, Int]](Json.parse(rateBandAsJson)) match {
      case JsSuccess(rateBands: SortedMap[LocalDate, Int], _) => getRateBand(date, rateBands) match {
        case Some((_, amount)) => amount
        case None => 0
      }
      case exception: JsError => throw new RuntimeException(exception.toString)
    })
  }
}
