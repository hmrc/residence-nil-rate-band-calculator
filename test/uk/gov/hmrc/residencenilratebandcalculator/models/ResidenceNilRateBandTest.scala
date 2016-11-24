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

class ResidenceNilRateBandTest extends UnitSpec {

  "Residence Nil Rate Band" must {

    "return 100000 when given a date of 1 Jan 2000" in {
      ResidenceNilRateBand(new LocalDate(2000, 1, 1)) shouldBe 100000
    }

    "return 100000 when given a date of 6 Apr 2017" in {
      ResidenceNilRateBand(new LocalDate(2017, 4, 6)) shouldBe 100000
    }

    "return 100000 when given a date of 5 Apr 2018" in {
      ResidenceNilRateBand(new LocalDate(2018, 4, 5)) shouldBe 100000
    }

    "return 125000 when given a date of 6 Apr 2018" in {
      ResidenceNilRateBand(new LocalDate(2018, 4, 6)) shouldBe 125000
    }

    "return 125000 when given a date of 5 Apr 2019" in {
      ResidenceNilRateBand(new LocalDate(2019, 4, 5)) shouldBe 125000
    }

    "return 150000 when given a date of 6 Apr 2019" in {
      ResidenceNilRateBand(new LocalDate(2019, 4, 6)) shouldBe 150000
    }

    "return 150000 when given a date of 5 Apr 2020" in {
      ResidenceNilRateBand(new LocalDate(2020, 4, 5)) shouldBe 150000
    }

    "return 175000 when given a date of 6 Apr 2020" in {
      ResidenceNilRateBand(new LocalDate(2020, 4, 6)) shouldBe 175000
    }

    "return 175000 when given a date of 5 Apr 2021" in {
      ResidenceNilRateBand(new LocalDate(2021, 4, 5)) shouldBe 175000
    }

    "return 175000 when given a date of 1 Jan 2040" in {
      ResidenceNilRateBand(new LocalDate(2040, 1, 1)) shouldBe 175000
    }
  }
}
