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

package uk.gov.hmrc.residencenilratebandcalculator.controllers

import java.io.ByteArrayInputStream

import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._
import org.scalatest.mockito.MockitoSugar
import play.api.Environment
import play.api.i18n.MessagesApi
import play.api.libs.json._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}

class BandControllerTest extends UnitSpec with WithFakeApplication with MockitoSugar {

  def fakeRequest = FakeRequest()
  def messagesApi = fakeApplication.injector.instanceOf[MessagesApi]
  def messages = messagesApi.preferred(fakeRequest)

  val env = mock[Environment]
  when(env.resourceAsStream(matches("data/RNRB-amounts-by-year.json"))) thenReturn Some(new ByteArrayInputStream(
    "{ \"2017-04-06\": 100000,  \"2018-04-06\": 125000,  \"2019-04-06\": 150000,  \"2020-04-06\": 175000}".getBytes))

  "Band Controller" must {

    "return 200 when requested with a valid date" in {
      val result = new BandController(env).getBand("2020-01-01")(fakeRequest)
      status(result) shouldBe 200
    }

    "return 400 when requested with an invalid date" in {
      val result = new BandController(env).getBand("not a date")(fakeRequest)
      status(result) shouldBe 400
    }
  }
}
