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

package uk.gov.hmrc.residencenilratebandcalculator.controllers

import java.io.ByteArrayInputStream

import org.mockito.Matchers._
import org.mockito.Mockito.when
import org.scalatest.mock.MockitoSugar
import play.api.Environment
import play.api.i18n.MessagesApi
import play.api.libs.json._
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}
import uk.gov.hmrc.residencenilratebandcalculator.models.Calculator

class CalculationControllerTest extends UnitSpec with WithFakeApplication with MockitoSugar {

  val env = mock[Environment]
  when(env.resourceAsStream(anyString)) thenReturn Some(new ByteArrayInputStream(
    "{ \"2017-04-06\": 100000,  \"2018-04-06\": 125000,  \"2019-04-06\": 150000,  \"2020-04-06\": 175000}".getBytes))
  val calculator = new Calculator(env)

  "Calculation Controller" must {

    val injector = fakeApplication.injector
    val messagesApi = injector.instanceOf[MessagesApi]

    "return a calculation result when given valid JSON" in {

      val json = Json.parse(
        """
          |{
          | "dateOfDeath": "2018-01-01",
          | "grossEstateValue": 0,
          | "propertyValue": 0,
          | "chargeableTransferAmount": 0,
          | "percentageCloselyInherited": 0,
          | "broughtForwardAllowance": 0
          |}
        """.stripMargin)

      val fakeRequest = FakeRequest("POST", "").withHeaders(("Content-Type", "application/json")).withBody(json)

      val response = new CalculationController(calculator, injector.instanceOf[MessagesApi]).calculate()(fakeRequest)

      status(response) shouldBe OK
      contentAsJson(response) shouldBe JsObject(Map("residenceNilRateAmount" -> JsNumber(0), "carryForwardAmount" -> JsNumber(100000)))
    }

    "return an error when given invalid JSON" in {

      val json = Json.parse(
        """
          |{
          | "dateOfDeath": "2018-01-01",
          | "grossEstateValue": -1,
          | "propertyValue": 0,
          | "chargeableTransferAmount": 0,
          | "percentageCloselyInherited": 0,
          | "broughtForwardAllowance": 0
          |}
        """.stripMargin)

      val fakeRequest = FakeRequest("POST", "").withHeaders(("Content-Type", "application/json")).withBody(json)

      val messages = messagesApi.preferred(fakeRequest)

      val response = new CalculationController(calculator, injector.instanceOf[MessagesApi]).calculate()(fakeRequest)

      status(response) shouldBe BAD_REQUEST
      (contentAsJson(response) \ "errors" \ "grossEstateValue").as[JsString].value shouldBe messages("error.expected.number.non_negative")
    }
  }
}
