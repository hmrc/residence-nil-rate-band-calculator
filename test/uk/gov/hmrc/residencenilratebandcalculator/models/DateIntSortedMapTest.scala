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

import common.CommonPlaySpec
import org.joda.time.LocalDate
import play.api.libs.json.Json
import uk.gov.hmrc.residencenilratebandcalculator.models.DateIntSortedMap._

import scala.collection.immutable.SortedMap

class DateIntSortedMapTest extends CommonPlaySpec {
  val json = Json.parse("""{"2018-01-01": 1, "2019-01-01": 2}""")
  val data = SortedMap[LocalDate, Int](new LocalDate(2019, 1, 1) -> 2, new LocalDate(2018, 1, 1) -> 1)

  "A Date to Int sorted map" must {
    "be parsable from valid JSON" in {
      Json.fromJson[SortedMap[LocalDate, Int]](json).get shouldBe data
    }

    "be writeable to JSON" in {
      Json.toJson[SortedMap[LocalDate, Int]](data) shouldBe json
    }

    "must fail when constructed with invalid JSON" in {
      Json.fromJson[SortedMap[LocalDate, Int]](Json.parse("{\"key\": []}")).asOpt shouldBe None
    }
  }
}
