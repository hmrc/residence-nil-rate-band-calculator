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

class CaseStudy11ASimpleCaseWithDownsizingSpec extends BaseComponentClass {

  "The calculate route" must {
    "return a valid OK response" when {
      "following case study 11.1 - A simple case" in {
        val testDownsizingDetails = DownsizingDetails(
          datePropertyWasChanged = LocalDate.parse("2018-06-01"),
          valueAvailableWhenPropertyChanged = 125000,
          valueOfChangedProperty = 195000,
          valueOfAssetsPassing = 850000
        )

        def request: Future[WSResponse] = ws
          .url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactoryWithDownsizing(
              dateOfDeath = LocalDate.of(2018, 5, 1),
              valueOfEstate = 2100000,
              propertyValue = 450000,
              chargeableEstateValue = 2100000,
              percentagePassedToDirectDescendants = 0,
              valueBeingTransferred = 0,
              downsizingDetails = testDownsizingDetails
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 0,
          applicableNilRateBandAmount = 125000,
          carryForwardAmount = 75000,
          defaultAllowanceAmount = 125000,
          adjustedAllowanceAmount = 75000
        )

        await(request).status shouldBe OK
        await(request).json shouldBe response
      }

    }
  }

}
