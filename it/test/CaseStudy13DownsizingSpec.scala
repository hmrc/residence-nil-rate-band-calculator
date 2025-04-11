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
import uk.gov.hmrc.residencenilratebandcalculator.models.DownsizingDetails
import scala.concurrent.Future
import play.api.libs.ws.JsonBodyWritables.writeableOf_JsValue

class CaseStudy13DownsizingSpec extends BaseComponentClass {

  "The calculate route" must {
    "return a valid OK response" when {
      "following case study 13.1 - A simple case of downsizing" in {
        val testDownsizingDetails = DownsizingDetails(
          datePropertyWasChanged = LocalDate.parse("2018-05-01"),
          valueAvailableWhenPropertyChanged = 0,
          valueOfChangedProperty = 500000,
          valueOfAssetsPassing = 200000
        )

        def request: Future[WSResponse] = ws
          .url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactoryWithDownsizing(
              dateOfDeath = LocalDate.of(2020, 9, 1),
              valueOfEstate = 305000,
              propertyValue = 105000,
              chargeableEstateValue = 305000,
              percentagePassedToDirectDescendants = 100,
              valueBeingTransferred = 0,
              downsizingDetails = testDownsizingDetails
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

      "following case study 13.2 - A simple case of downsizing" in {
        val testDownsizingDetails = DownsizingDetails(
          datePropertyWasChanged = LocalDate.parse("2018-05-01"),
          valueAvailableWhenPropertyChanged = 0,
          valueOfChangedProperty = 500000,
          valueOfAssetsPassing = 50000
        )

        def request: Future[WSResponse] = ws
          .url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactoryWithDownsizing(
              dateOfDeath = LocalDate.of(2020, 9, 1),
              valueOfEstate = 305000,
              propertyValue = 105000,
              chargeableEstateValue = 305000,
              percentagePassedToDirectDescendants = 100,
              valueBeingTransferred = 0,
              downsizingDetails = testDownsizingDetails
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 155000,
          applicableNilRateBandAmount = 175000,
          carryForwardAmount = 20000,
          defaultAllowanceAmount = 175000,
          adjustedAllowanceAmount = 175000
        )

        await(request).status shouldBe OK
        await(request).json shouldBe response
      }

    }
  }

}
