/*
 * Copyright 2023 HM Revenue & Customs
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

class CaseStudy8TransferOfRnrbWithPropertyInTrustSpec extends BaseComponentClass{

  "The calculate route" should{
    "return a valid OK response" when{
      "following case study 8.1 - A simple case" in{
        def request: Future[WSResponse] = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactory(
              dateOfDeath = LocalDate.of(2020, 12 ,1),
              valueOfEstate = 1200000,
              propertyValue = 400000,
              chargeableEstateValue = 1250000,
              percentagePassedToDirectDescendants = 100,
              valueBeingTransferred = 175000
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 350000,
          applicableNilRateBandAmount = 175000,
          carryForwardAmount = 0,
          defaultAllowanceAmount = 350000,
          adjustedAllowanceAmount =350000
        )

        await(request).status shouldBe OK
        await(request).json shouldBe response
      }
    }
  }
}
