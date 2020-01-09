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

import scala.collection.immutable.SortedMap
import scala.util.{Failure, Success, Try}

object DateTaperBandSortedMap extends SortedMapOrdering {

  val dateTaperBandSortedMapReads = new Reads[SortedMap[LocalDate, TaperBand]] {
    override def reads(json: JsValue) =
      Try(json.as[Map[String, TaperBand]].map {
        case (key: String, value: TaperBand) => (LocalDate.parse(key), value)
      }) match {
        case Success(bandsMap) => JsSuccess(SortedMap[LocalDate, TaperBand](bandsMap.toArray: _*))
        case Failure(error) =>
          Logger.error(error.getMessage, error)
          JsError(error.getMessage)
      }
    }

  val dateTaperBandSortedMapWrites = new Writes[SortedMap[LocalDate, TaperBand]] {
    override def writes(bandsMap: SortedMap[LocalDate, TaperBand]) = {
      Json.toJson[SortedMap[String, TaperBand]](bandsMap.map {
        case (key: LocalDate, value: TaperBand) => (key.toString, value)
      })
    }
  }

  implicit val dateIntSortedMapFormat = Format(dateTaperBandSortedMapReads, dateTaperBandSortedMapWrites)

}
