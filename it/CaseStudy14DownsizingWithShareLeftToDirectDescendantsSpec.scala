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

import org.joda.time.LocalDate
import java.time.{LocalDate => javaLocalDate}
import helpers.BaseComponentClass
import play.api.test.Helpers._
import play.api.libs.ws.WSResponse
import uk.gov.hmrc.residencenilratebandcalculator.models.{DownsizingDetails, PropertyValueAfterExemption}
import scala.concurrent.Future

class CaseStudy14DownsizingWithShareLeftToDirectDescendantsSpec extends BaseComponentClass{

  "The calculate route" must{
    "return a valid OK response" when{
      "following case study 14.1 - A simple case of downsizing with only part of the property left to descendants" in{
        val testDownsizingDetails = DownsizingDetails(
          datePropertyWasChanged = LocalDate.parse("2019-02-01"),
          valueAvailableWhenPropertyChanged = 0,
          valueOfChangedProperty = 400000,
          valueOfAssetsPassing = 150000
        )

        val testPropertyExemption = PropertyValueAfterExemption(
          value = 52500,
          inheritedValue = 52500
        )

        def request: Future[WSResponse] = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactoryWithExemptionAndDownsizing(
              dateOfDeath = javaLocalDate.of(2020,9,1),
              valueOfEstate = 700000,
              propertyValue = 105000,
              chargeableEstateValue = 700000,
              percentagePassedToDirectDescendants = 50,
              valueBeingTransferred = 0,
              propertyExemptions = testPropertyExemption,
              downsizingDetails = testDownsizingDetails
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 122500,
          applicableNilRateBandAmount = 175000,
          carryForwardAmount = 52500,
          defaultAllowanceAmount = 175000,
          adjustedAllowanceAmount =175000
        )

        await(request).status shouldBe OK
        await(request).json shouldBe response
      }

      "following case study 14.2 - A simple case of downsizing with only part of the property left to descendants" in{
        val testDownsizingDetails = DownsizingDetails(
          datePropertyWasChanged = LocalDate.parse("2019-02-01"),
          valueAvailableWhenPropertyChanged = 0,
          valueOfChangedProperty = 400000,
          valueOfAssetsPassing = 20000
        )

        val testPropertyExemption = PropertyValueAfterExemption(
          value = 52500,
          inheritedValue = 52500
        )

        def request: Future[WSResponse] = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactoryWithExemptionAndDownsizing(
              dateOfDeath = javaLocalDate.of(2020,9,1),
              valueOfEstate = 700000,
              propertyValue = 105000,
              chargeableEstateValue = 700000,
              percentagePassedToDirectDescendants = 50,
              valueBeingTransferred = 0,
              propertyExemptions = testPropertyExemption,
              downsizingDetails = testDownsizingDetails
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 72500,
          applicableNilRateBandAmount = 175000,
          carryForwardAmount = 102500,
          defaultAllowanceAmount = 175000,
          adjustedAllowanceAmount =175000
        )

        await(request).status shouldBe OK
        await(request).json shouldBe response
      }
    }
  }
}
