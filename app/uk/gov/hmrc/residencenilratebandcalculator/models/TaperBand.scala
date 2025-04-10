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

package uk.gov.hmrc.residencenilratebandcalculator.models

import play.api.libs.json._
import play.api.libs.json.Writes._
import play.api.libs.json.Reads._
import play.api.libs.functional.syntax._

case class TaperBand(threshold: Int, rate: Int)

object TaperBand {

  val taperBandReads: Reads[TaperBand] =
    (__ \ "threshold").read[Int].and((__ \ "rate").read[Int])(TaperBand.apply _)

  val taperBandWrites: Writes[TaperBand] =
    (__ \ "threshold").write[Int].and((__ \ "rate").write[Int])(o => Tuple.fromProductTyped(o))

  implicit val taperBandFormat: Format[TaperBand] = Format(taperBandReads, taperBandWrites)
}
