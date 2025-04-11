/*
 * Copyright 2024 HM Revenue & Customs
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

import java.time.LocalDate

import helpers.BaseComponentClass
import play.api.test.Helpers._
import play.api.libs.ws.WSResponse
import play.api.libs.ws.JsonBodyWritables.writeableOf_JsValue
import scala.concurrent.Future

class CaseStudy1SimpleCaseSpec extends BaseComponentClass {

  "The calculate route" should {
    "return a valid OK response" when {
      "following case study 1.1 - A simple case" in {
        def request: Future[WSResponse] = ws
          .url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactory(
              dateOfDeath = LocalDate.of(2021, 1, 1),
              valueOfEstate = 490000,
              propertyValue = 300000,
              chargeableEstateValue = 490000,
              percentagePassedToDirectDescendants = 100,
              valueBeingTransferred = 0
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 175000,
          applicableNilRateBandAmount = 175000,
          carryForwardAmount = 0,
          defaultAllowanceAmount = 175000,
          adjustedAllowanceAmount = 175000
        )
        await(request).status shouldBe OK
        await(request).json shouldBe response
      }

      "following case study 1.2 - Death occurs in an earlier tax year" in {
        def request: Future[WSResponse] = ws
          .url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactory(
              dateOfDeath = LocalDate.of(2018, 1, 1),
              valueOfEstate = 490000,
              propertyValue = 300000,
              chargeableEstateValue = 490000,
              percentagePassedToDirectDescendants = 100,
              valueBeingTransferred = 0
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 100000,
          applicableNilRateBandAmount = 100000,
          carryForwardAmount = 0,
          defaultAllowanceAmount = 100000,
          adjustedAllowanceAmount = 100000
        )

        await(request).status shouldBe OK
        await(request).json shouldBe response
      }
    }
  }

}
