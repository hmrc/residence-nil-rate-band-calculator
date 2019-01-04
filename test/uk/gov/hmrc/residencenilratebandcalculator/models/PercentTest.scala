/*
 * Copyright 2019 HM Revenue & Customs
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

import uk.gov.hmrc.play.test.UnitSpec

class PercentTest extends UnitSpec {

  "Percent" when {

    "created with a value of 0" must {

      "return 0 as a decimal" in {
        val p = new Percent(0)
        p.asDecimal shouldBe 0
      }
    }

    "created with a value of 100" must {

      "return 1 as a decimal" in {
        val p = new Percent(100)
        p.asDecimal shouldBe 1
      }
    }

    "created with a value of 45" must {

      "return 0.45 as a decimal" in {
        val p = new Percent(45)
        p.asDecimal shouldBe 0.45
      }
    }

    "compared to a larger value" must {
      "return -1" in {
        val p = Percent(10)
        val q = Percent(11)
        p compare q shouldBe -1
      }
    }

    "compared to a smaller value" must {
      "return 1" in {
        val p = Percent(10)
        val q = Percent(9)
        p compare q shouldBe 1
      }
    }

    "compared to an equal value" must {
      "return 0" in {
        val p = Percent(10)
        val q = Percent(10)
        p compare q shouldBe 0
      }
    }

    "multiplied by a double" must {
      "return the correct percentage of the double" in {
        val p = Percent(50)
        p * 100.2 shouldBe 50.1
      }
    }

    "multiplied by an Int" must {
      "return the correct percentage of the Int" in {
        val p = Percent(50)
        p * 100 shouldBe 50
      }
    }
  }
}
