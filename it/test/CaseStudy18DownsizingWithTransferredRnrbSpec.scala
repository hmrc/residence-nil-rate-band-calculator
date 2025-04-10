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

class CaseStudy18DownsizingWithTransferredRnrbSpec extends BaseComponentClass {

  "The calculate route" should {
    "return a valid OK response" when {
      "following case study 18.1 - A simple case of downsizing from a property which was worth less than the available RNRB" in {
        val testDownsizingDetails = DownsizingDetails(
          datePropertyWasChanged = LocalDate.parse("2018-10-01"),
          valueAvailableWhenPropertyChanged = 125000,
          valueOfChangedProperty = 285000,
          valueOfAssetsPassing = 250000
        )

        def request: Future[WSResponse] = ws
          .url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactoryWithDownsizing(
              dateOfDeath = LocalDate.of(2021, 3, 1),
              valueOfEstate = 500000,
              propertyValue = 0,
              chargeableEstateValue = 500000,
              percentagePassedToDirectDescendants = 0,
              valueBeingTransferred = 175000,
              downsizingDetails = testDownsizingDetails
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 250000,
          applicableNilRateBandAmount = 175000,
          carryForwardAmount = 100000,
          defaultAllowanceAmount = 350000,
          adjustedAllowanceAmount = 350000
        )

        await(request).status shouldBe OK
        await(request).json shouldBe response
      }
    }
  }

}
