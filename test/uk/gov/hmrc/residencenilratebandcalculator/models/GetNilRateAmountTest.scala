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

class GetNilRateAmountTest extends UnitSpec with WithFakeApplication with MockitoSugar {

  val rateBandsAsJson = "{ \"2017-04-06\": 100000,  \"2018-04-06\": 125000,  \"2019-04-06\": 150000,  \"2020-04-06\": 175000}"

  "Residence Nil Rate Band" must {

    "return 0 when given a date before the start date of the first RNRB band" in {
      GetNilRateAmount(new LocalDate(2000, 1, 1), rateBandsAsJson) shouldBe Success(0)
    }

    "return 100000 when given a date of 6 Apr 2017" in {
      GetNilRateAmount(new LocalDate(2017, 4, 6), rateBandsAsJson) shouldBe Success(100000)
    }

    "return 100000 when given a date of 5 Apr 2018" in {
      GetNilRateAmount(new LocalDate(2018, 4, 5), rateBandsAsJson) shouldBe Success(100000)
    }

    "return 125000 when given a date of 6 Apr 2018" in {
      GetNilRateAmount(new LocalDate(2018, 4, 6), rateBandsAsJson) shouldBe Success(125000)
    }

    "return 125000 when given a date of 5 Apr 2019" in {
      GetNilRateAmount(new LocalDate(2019, 4, 5), rateBandsAsJson) shouldBe Success(125000)
    }

    "return 150000 when given a date of 6 Apr 2019" in {
      GetNilRateAmount(new LocalDate(2019, 4, 6), rateBandsAsJson) shouldBe Success(150000)
    }

    "return 150000 when given a date of 5 Apr 2020" in {
      GetNilRateAmount(new LocalDate(2020, 4, 5), rateBandsAsJson) shouldBe Success(150000)
    }

    "return 175000 when given a date of 6 Apr 2020" in {
      GetNilRateAmount(new LocalDate(2020, 4, 6), rateBandsAsJson) shouldBe Success(175000)
    }

    "return 175000 when given a date of 5 Apr 2021" in {
      GetNilRateAmount(new LocalDate(2021, 4, 5), rateBandsAsJson) shouldBe Success(175000)
    }

    "return 175000 when given a date of 1 Jan 2040" in {
      GetNilRateAmount(new LocalDate(2040, 1, 1), rateBandsAsJson) shouldBe Success(175000)
    }

    "return a failure when invalid JSON is provided" in {
      GetNilRateAmount(new LocalDate(2017, 4, 6), "[") shouldBe a[Failure[_]]
    }

    "return a failure when the provided JSON does not represent a rate band" in {
      val result = GetNilRateAmount(new LocalDate(2017, 4, 6), "{\"something\": []}")
      println("result: " + result)
      result shouldBe a[Failure[_]]
    }
  }
}
