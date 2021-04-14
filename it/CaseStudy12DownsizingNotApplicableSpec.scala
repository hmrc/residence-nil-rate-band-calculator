/*
 * Copyright 2021 HM Revenue & Customs
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

class CaseStudy12DownsizingNotApplicableSpec extends BaseComponentClass{

  "The calculate route" must{
    "return a valid OK response" when{
      "following case study 12.1 - A simple case where downsizing is not due" in{
        val testDownsizingDetails = DownsizingDetails(
          datePropertyWasChanged = LocalDate.parse("2018-10-01"),
          valueAvailableWhenPropertyChanged = 0,
          valueOfChangedProperty = 450000,
          valueOfAssetsPassing = 500000
        )

        def request: Future[WSResponse] = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactoryWithDownsizing(
              dateOfDeath = javaLocalDate.of(2020,8,1),
              valueOfEstate = 700000,
              propertyValue = 200000,
              chargeableEstateValue = 700000,
              percentagePassedToDirectDescendants = 0,
              valueBeingTransferred = 0,
              downsizingDetails = testDownsizingDetails
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 0,
          applicableNilRateBandAmount = 175000,
          carryForwardAmount = 175000,
          defaultAllowanceAmount = 175000,
          adjustedAllowanceAmount =175000
        )

        await(request).status shouldBe OK
        await(request).json shouldBe response
      }

    }
  }
}
