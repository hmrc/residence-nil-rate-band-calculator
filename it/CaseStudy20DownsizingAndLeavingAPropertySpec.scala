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

import org.joda.time.LocalDate
import java.time.{LocalDate => javaLocalDate}
import helpers.BaseComponentClass
import play.api.test.Helpers._
import play.api.libs.ws.WSResponse
import uk.gov.hmrc.residencenilratebandcalculator.models.DownsizingDetails
import scala.concurrent.Future

class CaseStudy20DownsizingAndLeavingAPropertySpec extends BaseComponentClass {

  "The calculate route" should{
    "return a valid OK response" when{
      "following case study 20.1 - downsizing and leaving a property with Value Being Transferred" in{
        val testDownsizingDetails = DownsizingDetails(
          datePropertyWasChanged = LocalDate.parse("2019-07-01"),
          valueAvailableWhenPropertyChanged = 150000,
          valueOfChangedProperty = 285000,
          valueOfAssetsPassing = 1660000
        )

        def request: Future[WSResponse] = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactoryWithDownsizing(
              dateOfDeath = javaLocalDate.of(2019,12,1),
              valueOfEstate = 1900000,
              propertyValue = 240000,
              chargeableEstateValue = 1900000,
              percentagePassedToDirectDescendants = 100,
              valueBeingTransferred = 150000,
              downsizingDetails = testDownsizingDetails
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 285000,
          applicableNilRateBandAmount = 150000,
          carryForwardAmount = 15000,
          defaultAllowanceAmount = 300000,
          adjustedAllowanceAmount =300000
        )

        await(request).status shouldBe OK
        await(request).json shouldBe response
      }

      "following case study 20.2 - downsizing and leaving a property with Value Being Transferred" in{
        val testDownsizingDetails = DownsizingDetails(
          datePropertyWasChanged = LocalDate.parse("2017-07-01"),
          valueAvailableWhenPropertyChanged = 100000,
          valueOfChangedProperty = 285000,
          valueOfAssetsPassing = 1660000
        )

        def request: Future[WSResponse] = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactoryWithDownsizing(
              dateOfDeath = javaLocalDate.of(2019,12,1),
              valueOfEstate = 1900000,
              propertyValue = 240000,
              chargeableEstateValue = 1900000,
              percentagePassedToDirectDescendants = 100,
              valueBeingTransferred = 150000,
              downsizingDetails = testDownsizingDetails
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
    }
  }

}
