import java.time.LocalDate

import helpers.BaseComponentClass
import play.api.http.Status._
import play.api.libs.ws.WSResponse

class CaseStudy6ShareLeftToDirectDescendants extends BaseComponentClass{

  "The calculate route" should{
    "return a valid OK response" when{
      "following case study 6.1 - A simple case" in{
        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactory(
              dateOfDeath = LocalDate.of(2021, 1 ,1),
              valueOfEstate = 800000,
              propertyValue = 500000,
              chargeableEstateValue = 800000,
              percentagePassedToDirectDescendants = 50.01,
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

      "following case study 6.2 - Amount left to direct descendants is less than the maximum RNRB" in{
        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactory(
              dateOfDeath = LocalDate.of(2021, 1 ,1),
              valueOfEstate = 750000,
              propertyValue = 250000,
              chargeableEstateValue = 750000,
              percentagePassedToDirectDescendants = 50.01,
              valueBeingTransferred = 0
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 125025,
          applicableNilRateBandAmount = 175000,
          carryForwardAmount = 49975,
          defaultAllowanceAmount = 175000,
          adjustedAllowanceAmount =175000
        )

        request.status shouldBe OK
        request.json shouldBe response
      }
    }
  }
}
