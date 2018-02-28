/*
 * Copyright 2018 HM Revenue & Customs
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
import org.scalatest.mock.MockitoSugar
import org.mockito.Matchers._
import org.mockito.Mockito.when
import play.api.Environment
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}

import scala.util.Success

class GetTaperBandFromFileTest extends UnitSpec with WithFakeApplication with MockitoSugar {

  val env = mock[Environment]
  when(env.resourceAsStream(anyString)) thenReturn Some(new ByteArrayInputStream(
    """{ "2017-04-06": {"threshold": 1, "rate": 2}, "2020-04-06": {"threshold": 3, "rate": 4}}""".getBytes))

  val getTaperBandFromFile = new GetTaperBandFromFile(env, "")

  "Get Taper Band From File" must {

    "return TaperBand(0, 1) when given a date before the first taper band" in {
      getTaperBandFromFile(new LocalDate(2017, 4, 5)) shouldBe Success(TaperBand(0, 1))
    }

    "return the correct value when given a date equal to the start of the first band" in {
      getTaperBandFromFile(new LocalDate(2017, 4, 6)) shouldBe Success(TaperBand(1, 2))
    }

    "return the correct value when given a date between two bands" in {
      getTaperBandFromFile(new LocalDate(2017, 4, 7)) shouldBe Success(TaperBand(1, 2))
    }

    "return the correct value when given a date equal to the start of the second band" in {
      getTaperBandFromFile(new LocalDate(2020, 4, 6)) shouldBe Success(TaperBand(3, 4))
    }

    "return the correct value when given a date after the last band" in {
      getTaperBandFromFile(new LocalDate(2020, 4, 7)) shouldBe Success(TaperBand(3, 4))
    }

    "fail when unable to obtain the taper bands as JSON" in {
      val env = mock[Environment]
      when(env.resourceAsStream(anyString)) thenReturn None
      val getTaperBandFromFile = new GetTaperBandFromFile(env, "")

      getTaperBandFromFile(new LocalDate(2006, 4, 6)).failed.get.getMessage shouldBe "error.resource_access_failure"

    }
  }
}
