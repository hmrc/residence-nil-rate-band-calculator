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

class CaseStudy23DownsizingAndLeavingAPropertySpec extends BaseComponentClass{

  "The calculate route" must{
    "return a valid OK response" when{
      "following case study 23.1 - downsizing and leaving a property with Value Being Transferred" in{
        val testDownsizingDetails = DownsizingDetails(
          datePropertyWasChanged = LocalDate.parse("2018-10-01"),
          valueAvailableWhenPropertyChanged = 125000,
          valueOfChangedProperty = 500000,
          valueOfAssetsPassing = 710000
        )

        def request: Future[WSResponse] = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactoryWithDownsizing(
              dateOfDeath = LocalDate.of(2020,5,1),
              valueOfEstate = 800000,
              propertyValue = 90000,
              chargeableEstateValue = 800000,
              percentagePassedToDirectDescendants = 100,
              valueBeingTransferred = 175000,
              downsizingDetails = testDownsizingDetails
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
