/*
 * Copyright 2023 HM Revenue & Customs
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
import play.api.Logging
import play.api.libs.json._

import scala.collection.immutable.SortedMap
import scala.util.{Failure, Success, Try}

class InvalidJsonException extends RuntimeException

object DateIntSortedMap extends SortedMapOrdering with Logging {

  val dateIntSortedMapReads = new Reads[SortedMap[LocalDate, Int]] {
    override def reads(json: JsValue) =
      Try(json.as[Map[String, Int]].map {
        case (key: String, value: Int) => (LocalDate.parse(key), value)
      }) match {
        case Success(bandsMap) => JsSuccess(SortedMap[LocalDate, Int](bandsMap.toArray: _*))
        case Failure(error) => {
          logger.error(error.getMessage, error)
          JsError(error.getMessage)
        }
      }
  }

  val dateIntSortedMapWrites = new Writes[SortedMap[LocalDate, Int]] {
    override def writes(bandsMap: SortedMap[LocalDate, Int]) = {
      Json.toJson[SortedMap[String, Int]](bandsMap.map {
        case (key: LocalDate, value: Int) => (key.toString, value)
      })
    }
  }

  implicit val dateIntSortedMapFormat = Format(dateIntSortedMapReads, dateIntSortedMapWrites)
}
