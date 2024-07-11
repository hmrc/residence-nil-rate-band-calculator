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

class CaseStudy15DownsizingBefore2017Spec extends BaseComponentClass {

  "The calculate route" must{
    "return a valid OK response" when{
      "following case study 15.1 - A simple case of downsizing before 6 April 2017" in{
        val testDownsizingDetails = DownsizingDetails(
          datePropertyWasChanged = LocalDate.parse("2016-03-01"),
          valueAvailableWhenPropertyChanged = 0,
          valueOfChangedProperty = 150000,
          valueOfAssetsPassing = 80000
        )

        def request: Future[WSResponse] = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactoryWithDownsizing(
              dateOfDeath = LocalDate.of(2019,12,1),
              valueOfEstate = 185000,
              propertyValue = 105000,
              chargeableEstateValue = 80000,
              percentagePassedToDirectDescendants = 0,
              valueBeingTransferred = 0,
              downsizingDetails = testDownsizingDetails
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 45000,
          applicableNilRateBandAmount = 150000,
          carryForwardAmount = 105000,
          defaultAllowanceAmount = 150000,
          adjustedAllowanceAmount =150000
        )

        await(request).status shouldBe OK
        await(request).json shouldBe response
      }

      "following case study 15.2 - A simple case of downsizing before 6 April 2017" in{
        val testDownsizingDetails = DownsizingDetails(
          datePropertyWasChanged = LocalDate.parse("2016-03-01"),
          valueAvailableWhenPropertyChanged = 0,
          valueOfChangedProperty = 150000,
          valueOfAssetsPassing = 10000
        )

        def request: Future[WSResponse] = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactoryWithDownsizing(
              dateOfDeath = LocalDate.of(2019,12,1),
              valueOfEstate = 185000,
              propertyValue = 0,
              chargeableEstateValue = 80000,
              percentagePassedToDirectDescendants = 0,
              valueBeingTransferred = 0,
              downsizingDetails = testDownsizingDetails
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 10000,
          applicableNilRateBandAmount = 150000,
          carryForwardAmount = 140000,
          defaultAllowanceAmount = 150000,
          adjustedAllowanceAmount =150000
        )

        await(request).status shouldBe OK
        await(request).json shouldBe response
      }
    }
  }
}
