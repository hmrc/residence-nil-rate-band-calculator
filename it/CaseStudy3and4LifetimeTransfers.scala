import java.time.LocalDate

import helpers.BaseComponentClass
import play.api.http.Status._
import play.api.libs.ws.WSResponse

class CaseStudy3and4LifetimeTransfers extends BaseComponentClass {

  "The calculate route" should{
    "return a valid OK response" when{
      "following case study 3.1 - A simple case" in{
        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactory(
              dateOfDeath = LocalDate.of(2021, 1 ,1),
              valueOfEstate = 450000,
              propertyValue = 200000,
              chargeableEstateValue = 450000,
              percentagePassedToDirectDescendants = 100,
              valueBeingTransferred = 0
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 175000,
          applicableNilRateBandAmount = 175000,
          carryForwardAmount = 0,
          defaultAllowanceAmount = 175000,
          adjustedAllowanceAmount =175000
        )

        request.status shouldBe OK
        request.json shouldBe response
      }

      "following case study 4.1 - A simple case" in{
        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactory(
              dateOfDeath = LocalDate.of(2019, 1 ,1),
              valueOfEstate = 750000,
              propertyValue = 500000,
              chargeableEstateValue = 750000,
              percentagePassedToDirectDescendants = 100,
              valueBeingTransferred = 0
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 175000,
          applicableNilRateBandAmount = 175000,
          carryForwardAmount = 0,
          defaultAllowanceAmount = 175000,
          adjustedAllowanceAmount =175000
        )

        request.status shouldBe OK
        request.json shouldBe response
      }
    }
  }
}
