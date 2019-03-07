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

package uk.gov.hmrc.residencenilratebandcalculator.controllers

import java.io.ByteArrayInputStream

import org.mockito.Mockito._
import org.mockito.ArgumentMatchers._
import org.scalatest.mockito.MockitoSugar
import play.api.Environment
import play.api.i18n.MessagesApi
import play.api.libs.json._
import play.api.mvc.{ControllerComponents, PlayBodyParsers}
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}
import uk.gov.hmrc.residencenilratebandcalculator.models.{CalculationInput, Calculator}

import scala.util.Failure

class CalculationControllerTest extends UnitSpec with WithFakeApplication with MockitoSugar {

  def fakeRequest = FakeRequest()
  def messagesApi = fakeApplication.injector.instanceOf[MessagesApi]
  def injectedComps = fakeApplication.injector.instanceOf[ControllerComponents]
  def injectedParsers = fakeApplication.injector.instanceOf[PlayBodyParsers]
  def messages = messagesApi.preferred(fakeRequest)

  val env = mock[Environment]
  when(env.resourceAsStream(matches("data/RNRB-amounts-by-year.json"))) thenReturn Some(new ByteArrayInputStream(
    "{ \"2017-04-06\": 100000,  \"2018-04-06\": 125000,  \"2019-04-06\": 150000,  \"2020-04-06\": 175000}".getBytes))

  when(env.resourceAsStream(matches("data/Taper-bands-by-year.json"))) thenReturn Some(new ByteArrayInputStream(
    """{ "2017-04-06": {"threshold": 2000000, "rate": 2}}""".getBytes))

  val calculator = new Calculator(env)

  "Calculation Controller" must {

    "return a calculation result when given valid JSON" in {

      val json = Json.parse(
        """
          |{
          | "dateOfDeath": "2018-01-01",
          | "valueOfEstate": 0,
          | "propertyValue": 0,
          | "chargeableEstateValue": 0,
          | "percentagePassedToDirectDescendants": 0,
          | "valueBeingTransferred": 0
          |}
        """.stripMargin)

      val fakeRequest = FakeRequest("POST", "").withHeaders(("Content-Type", "application/json")).withBody(json)

      val response = new CalculationController(calculator)(injectedComps, injectedParsers).calculate()(fakeRequest)

      status(response) shouldBe OK
      contentAsJson(response) shouldBe JsObject(Map(
        "residenceNilRateAmount" -> JsNumber(0),
        "applicableNilRateBandAmount" -> JsNumber(100000),
        "carryForwardAmount" -> JsNumber(100000),
        "defaultAllowanceAmount" -> JsNumber(100000),
        "adjustedAllowanceAmount" -> JsNumber(100000)))
    }

    "return an error when given invalid JSON" in {

      val json = Json.parse(
        """
          |{
          | "dateOfDeath": "2018-01-01",
          | "valueOfEstate": -1,
          | "propertyValue": 0,
          | "chargeableEstateValue": 0,
          | "percentagePassedToDirectDescendants": 0,
          | "valueBeingTransferred": 0
          |}
        """.stripMargin)

      val fakeRequest = FakeRequest("POST", "").withHeaders(("Content-Type", "application/json")).withBody(json)

      val response = new CalculationController(calculator)(injectedComps, injectedParsers).calculate()(fakeRequest)

      status(response) shouldBe BAD_REQUEST
      (contentAsJson(response) \ "errors" \ "valueOfEstate").as[JsString].value shouldBe messages("error.expected.number.non_negative")
    }

    "return an error when the calculation fails" in {

      val json = Json.parse(
        """
          |{
          | "dateOfDeath": "2018-01-01",
          | "valueOfEstate": 0,
          | "propertyValue": 0,
          | "chargeableEstateValue": 0,
          | "percentagePassedToDirectDescendants": 0,
          | "valueBeingTransferred": 0
          |}
        """.stripMargin)

      val fakeRequest = FakeRequest("POST", "").withHeaders(("Content-Type", "application/json")).withBody(json)

      val mockCalculator = mock[Calculator]
      when(mockCalculator.apply(any[CalculationInput])) thenReturn Failure(new RuntimeException("error.resource_access_failure"))

      val response = new CalculationController(mockCalculator)(injectedComps, injectedParsers).calculate()(fakeRequest)

      status(response) shouldBe INTERNAL_SERVER_ERROR
      (contentAsJson(response) \ "message").as[JsString].value shouldBe messages("error.resource_access_failure")
    }
  }
}
