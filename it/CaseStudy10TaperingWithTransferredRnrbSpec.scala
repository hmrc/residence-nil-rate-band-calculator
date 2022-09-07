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

class CaseStudy10TaperingWithTransferredRnrbSpec extends BaseComponentClass {

  "The calculate route" must{
    "return a valid OK response" when{
      "following case study 10.1 - A simple case" in{
        def request: Future[WSResponse] = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactory(
              dateOfDeath = LocalDate.of(2018,5,1),
              valueOfEstate = 2100000,
              propertyValue = 4500010,
              chargeableEstateValue = 2100000,
              percentagePassedToDirectDescendants = 0,
              valueBeingTransferred = 0
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 0,
          applicableNilRateBandAmount = 125000,
          carryForwardAmount = 75000,
          defaultAllowanceAmount = 125000,
          adjustedAllowanceAmount =75000
        )

        await(request).status shouldBe OK
        await(request).json shouldBe response
      }

      "following case study 10.2" in{
        def request: Future[WSResponse] = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactory(
              dateOfDeath = LocalDate.of(2021,3,25),
              valueOfEstate = 1800000,
              propertyValue = 500000,
              chargeableEstateValue = 1800000,
              percentagePassedToDirectDescendants = 100,
              valueBeingTransferred = 105000
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 280000,
          applicableNilRateBandAmount = 175000,
          carryForwardAmount = 0,
          defaultAllowanceAmount = 280000,
          adjustedAllowanceAmount =280000
        )

        await(request).status shouldBe OK
        await(request).json shouldBe response
      }
    }
  }
}
