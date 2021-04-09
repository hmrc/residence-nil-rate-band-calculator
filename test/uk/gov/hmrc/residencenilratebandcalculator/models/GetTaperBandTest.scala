/*
 * Copyright 2021 HM Revenue & Customs
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
import org.scalatestplus.mockito.MockitoSugar
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}

import scala.util.{Failure, Success}

class GetTaperBandTest  extends UnitSpec with WithFakeApplication with MockitoSugar {

  val basedAsJson = """{ "2017-04-06": {"threshold": 1, "rate": 2}, "2020-04-06": {"threshold": 3, "rate": 4}}"""

  "Get Taper Band" must {

    "return TaperBand(0, 1) when given a date before the start date of the first band" in {
      GetTaperBand(new LocalDate(2017, 4, 5), basedAsJson) shouldBe Success(TaperBand(0, 1))
    }

    "return the correct value when given a date equal to the start of the first band" in {
      GetTaperBand(new LocalDate(2017, 4, 6), basedAsJson) shouldBe Success(TaperBand(1, 2))
    }

    "return the correct value when given a date between two bands" in {
      GetTaperBand(new LocalDate(2017, 4, 7), basedAsJson) shouldBe Success(TaperBand(1, 2))
    }

    "return the correct value when given a date equal to the start of the second band" in {
      GetTaperBand(new LocalDate(2020, 4, 6), basedAsJson) shouldBe Success(TaperBand(3, 4))
    }

    "return the correct value when given a date after the last band" in {
      GetTaperBand(new LocalDate(2020, 4, 7), basedAsJson) shouldBe Success(TaperBand(3, 4))
    }

    "return a failure when invalid JSON is provided" in {
      GetTaperBand(new LocalDate(2017, 4, 6), "[") shouldBe a[Failure[_]]
    }

    "return a failure when the provided JSON does not represent a rate band" in {
      val result = GetTaperBand(new LocalDate(2017, 4, 6), "{\"something\": []}")
      result shouldBe a[Failure[_]]
    }
  }
}
