/*
 * Copyright 2025 HM Revenue & Customs
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

import java.time.LocalDate
import org.scalatest.matchers.should.Matchers
import uk.gov.hmrc.residencenilratebandcalculator.helpers.SimpleBandHelper

class BandTest extends CommonPlaySpec with Matchers {

  "The Band trait" should {

    "return the correct value for a given date using apply".which {
      val bands = Map(
        LocalDate.of(2023, 1, 1)  -> 100,
        LocalDate.of(2023, 6, 1)  -> 200,
        LocalDate.of(2023, 12, 1) -> 300
      )
      val band = new SimpleBandHelper(bands)

      "Test a date with a matching band" in {
        band(LocalDate.of(2023, 6, 15)) shouldBe 200
      }

      "Test for the highest date before a given date" in {
        band(LocalDate.of(2024, 1, 1)) shouldBe 300
      }

      "Test for a date without any prior dates" in {
        band(LocalDate.of(2022, 12, 31)) shouldBe 0
      }
    }

    "correctly determine the highest date before a given date".which {
      val bands = Map(
        LocalDate.of(2023, 1, 1)  -> 100,
        LocalDate.of(2023, 6, 1)  -> 200,
        LocalDate.of(2023, 12, 1) -> 300
      )
      val band = new SimpleBandHelper(bands)

      "Test highest date before" in {
        band.getHighestDateBefore(LocalDate.of(2023, 7, 1), bands) shouldBe Option(LocalDate.of(2023, 6, 1))
        band.getHighestDateBefore(LocalDate.of(2023, 1, 2), bands) shouldBe Option(LocalDate.of(2023, 1, 1))
      }

      "Test when no previous date exists" in {
        band.getHighestDateBefore(LocalDate.of(2023, 1, 1), bands).isEmpty shouldBe true
      }
    }

    "handle edge cases correctly" in {
      val emptyBands = Map.empty[LocalDate, Int]
      val band       = new SimpleBandHelper(emptyBands)

      band(LocalDate.now()) shouldBe 0
      band.getHighestDateBefore(LocalDate.now(), emptyBands).isEmpty shouldBe true
    }
  }

}
