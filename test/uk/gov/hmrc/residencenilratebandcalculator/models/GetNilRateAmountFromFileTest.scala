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

import java.io.ByteArrayInputStream

import org.joda.time.LocalDate
import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._
import org.scalatestplus.mockito.MockitoSugar
import play.api.Environment
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}

import scala.util.Success

class GetNilRateAmountFromFileTest extends UnitSpec with WithFakeApplication with MockitoSugar {

  val env = mock[Environment]
  when(env.resourceAsStream(anyString)) thenReturn Some(new ByteArrayInputStream(
    """{"2006-04-06": 285000, "2007-04-06": 300000, "2008-04-06": 312000, "2009-04-06": 325000}""".getBytes))

  val getNilRateAmountFromFile = new GetNilRateAmountFromFile(env, "")

  "Get Nil Rate Amount From File" must {

    "return 0 when given a date of 1 Jan 2000" in {
      getNilRateAmountFromFile(new LocalDate(2000, 1, 1)) shouldBe Success(0)
    }

    "return 285,000 when given a date of 6 Apr 2006" in {
      getNilRateAmountFromFile(new LocalDate(2006, 4, 6)) shouldBe Success(285000)
    }

    "return 300,000 when given a date of 6 Apr 2007" in {
      getNilRateAmountFromFile(new LocalDate(2007, 4, 6)) shouldBe Success(300000)
    }

    "return 312,000 when given a date of 6 Apr 2008" in {
      getNilRateAmountFromFile(new LocalDate(2008, 4, 6)) shouldBe Success(312000)
    }

    "return 325,000 when given a date of 6 Apr 2009" in {
      getNilRateAmountFromFile(new LocalDate(2009, 4, 6)) shouldBe Success(325000)
    }

    "return 325,000 when given a date of 1 Jan 2040" in {
      getNilRateAmountFromFile(new LocalDate(2040, 1, 1)) shouldBe Success(325000)
    }

    "fail when unable to obtain the rate bands as JSON" in {
      val env = mock[Environment]
      when(env.resourceAsStream(anyString)) thenReturn None
      val getNilRateAmountFromFile = new GetNilRateAmountFromFile(env, "")

      getNilRateAmountFromFile(new LocalDate(2006, 4, 6)).failed.get.getMessage shouldBe "error.resource_access_failure"
    }
  }
}
