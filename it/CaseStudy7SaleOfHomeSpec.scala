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
import play.api.libs.ws.WSResponse
import play.api.test.Helpers._
import scala.concurrent.Future

class CaseStudy7SaleOfHomeSpec extends BaseComponentClass{

  "The calculate route" should{
    "return a valid OK response" when{
      "following case study 7.1 - A simple case" in{
        def request: Future[WSResponse] = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactory(
              dateOfDeath = LocalDate.of(2019, 12 ,1),
              valueOfEstate = 900000,
              propertyValue = 500000,
              chargeableEstateValue = 900000,
              percentagePassedToDirectDescendants = 100,
              valueBeingTransferred = 0
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 150000,
          applicableNilRateBandAmount = 150000,
          carryForwardAmount = 0,
          defaultAllowanceAmount = 150000,
          adjustedAllowanceAmount =150000
        )

        await(request).status shouldBe OK
        await(request).json shouldBe response
      }
    }
  }
}
