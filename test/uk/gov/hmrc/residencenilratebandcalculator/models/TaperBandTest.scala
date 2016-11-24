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
import uk.gov.hmrc.play.test.UnitSpec

class TaperBandTest extends UnitSpec {

  "Taper Band" must {

    "return 2000000 when given a date of 1 Jan 2000" in {
      TaperBand(new LocalDate(2000, 1, 1)) shouldBe 2000000
    }

    "return 2000000 when given a date of 6 Apr 2017" in {
      TaperBand(new LocalDate(2017, 4, 6)) shouldBe 2000000
    }

    "return 2000000 when given a date of 5 Apr 2021" in {
      TaperBand(new LocalDate(2021, 4, 5)) shouldBe 2000000
    }

    "return 2000000 when given a date of 1 Jan 2040" in {
      TaperBand(new LocalDate(2040, 1, 1)) shouldBe 2000000
    }
  }
}
