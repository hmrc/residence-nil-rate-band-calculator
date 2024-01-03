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
import play.api.libs.ws.WSResponse
import play.api.test.Helpers._

import scala.concurrent.Future

class CaseStudy5TransferOfUnusedRnrbSpec extends BaseComponentClass {

  "The calculate route" should{
    "return a valid OK response" when{
      "following case study 5.1 - A simple case" in{
        def request: Future[WSResponse] = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactory(
              dateOfDeath = LocalDate.of(2019, 7 ,30),
              valueOfEstate = 1000000,
              propertyValue = 400000,
              chargeableEstateValue = 1000000,
              percentagePassedToDirectDescendants = 100,
              valueBeingTransferred = 150000
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 300000,
          applicableNilRateBandAmount = 150000,
          carryForwardAmount = 0,
          defaultAllowanceAmount = 300000,
          adjustedAllowanceAmount =300000
        )

        await(request).status shouldBe OK
        await(request).json shouldBe response
      }

      "following case study 5.2 - Property worth less than the RNRB + unused allowance" in{
        def request: Future[WSResponse] = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactory(
              dateOfDeath = LocalDate.of(2019, 7 ,30),
              valueOfEstate = 750000,
              propertyValue = 250000,
              chargeableEstateValue = 750000,
              percentagePassedToDirectDescendants = 100,
              valueBeingTransferred = 150000
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 250000,
          applicableNilRateBandAmount = 150000,
          carryForwardAmount = 50000,
          defaultAllowanceAmount = 300000,
          adjustedAllowanceAmount = 300000
        )

        await(request).status shouldBe OK
        await(request).json shouldBe response
      }
    }
  }
}
