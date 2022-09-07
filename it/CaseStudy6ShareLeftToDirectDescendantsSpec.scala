/*
 * Copyright 2022 HM Revenue & Customs
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

import scala.concurrent.Future

class CaseStudy6ShareLeftToDirectDescendantsSpec extends BaseComponentClass{

  "The calculate route" should{
    "return a valid OK response" when{
      "following case study 6.1 - A simple case" in{
        def request: Future[WSResponse] = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactory(
              dateOfDeath = LocalDate.of(2021, 1 ,1),
              valueOfEstate = 800000,
              propertyValue = 500000,
              chargeableEstateValue = 800000,
              percentagePassedToDirectDescendants = 50.01,
              valueBeingTransferred = 0
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 175000,
          applicableNilRateBandAmount = 175000,
          carryForwardAmount = 0,
          defaultAllowanceAmount = 175000,
          adjustedAllowanceAmount =175000
        )

        await(request).status shouldBe OK
        await(request).json shouldBe response
      }

      "following case study 6.2 - Amount left to direct descendants is less than the maximum RNRB" in{
        def request: Future[WSResponse] = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactory(
              dateOfDeath = LocalDate.of(2021, 1 ,1),
              valueOfEstate = 750000,
              propertyValue = 250000,
              chargeableEstateValue = 750000,
              percentagePassedToDirectDescendants = 50.01,
              valueBeingTransferred = 0
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 125025,
          applicableNilRateBandAmount = 175000,
          carryForwardAmount = 49975,
          defaultAllowanceAmount = 175000,
          adjustedAllowanceAmount =175000
        )

        await(request).status shouldBe OK
        await(request).json shouldBe response
      }
    }
  }
}
