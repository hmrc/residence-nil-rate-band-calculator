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

class CaseStudy2HomeWorthLessThanMaximumRnrbSpec extends BaseComponentClass{

  "The calculate route" should{
    "return a valid OK response" when{
      "following case study 2.1 - A simple case" in{
        def request: Future[WSResponse] = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactory(
              dateOfDeath = LocalDate.of(2021, 1 ,1),
              valueOfEstate = 1000000,
              propertyValue = 100000,
              chargeableEstateValue = 500000,
              percentagePassedToDirectDescendants = 100,
              valueBeingTransferred = 0
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 100000,
          applicableNilRateBandAmount = 175000,
          carryForwardAmount = 75000,
          defaultAllowanceAmount = 175000,
          adjustedAllowanceAmount =175000
        )

        await(request).status shouldBe OK
        await(request).json shouldBe response
      }

      "following case study 2.2 - Death occurs in an earlier tax year" in{
        def request: Future[WSResponse] = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactory(
              dateOfDeath = LocalDate.of(2019, 1 ,1),
              valueOfEstate = 1000000,
              propertyValue = 100000,
              chargeableEstateValue = 500000,
              percentagePassedToDirectDescendants = 100,
              valueBeingTransferred = 0
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 100000,
          applicableNilRateBandAmount = 125000,
          carryForwardAmount = 25000,
          defaultAllowanceAmount = 125000,
          adjustedAllowanceAmount =125000
        )

        await(request).status shouldBe OK
        await(request).json shouldBe response
      }
    }
  }
}
