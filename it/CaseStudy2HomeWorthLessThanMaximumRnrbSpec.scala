import java.time.LocalDate

import helpers.BaseComponentClass
import play.api.http.Status._
import play.api.libs.ws.WSResponse

class CaseStudy2HomeWorthLessThanMaximumRnrbSpec extends BaseComponentClass{

  "The calculate route" should{
    "return a valid OK response" when{
      "following case study 2.1 - A simple case" in{
        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactory(
              dateOfDeath = LocalDate.of(2021, 1 ,1),
              valueOfEstate = 1000000,
              propertyValue = 100000,
              chargeableEstateValue = 500000,
              percentagePassedToDirectDescendants = 100,
              valueBeingTransferred = 0
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 100000,
          applicableNilRateBandAmount = 175000,
          carryForwardAmount = 75000,
          defaultAllowanceAmount = 175000,
          adjustedAllowanceAmount =175000
        )

        request.status shouldBe OK
        request.json shouldBe response
      }

      "following case study 2.2 - Death occurs in an earlier tax year" in{
        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactory(
              dateOfDeath = LocalDate.of(2019, 1 ,1),
              valueOfEstate = 1000000,
              propertyValue = 100000,
              chargeableEstateValue = 500000,
              percentagePassedToDirectDescendants = 100,
              valueBeingTransferred = 0
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 100000,
          applicableNilRateBandAmount = 125000,
          carryForwardAmount = 25000,
          defaultAllowanceAmount = 125000,
          adjustedAllowanceAmount =125000
        )

        request.status shouldBe OK
        request.json shouldBe response
      }
    }
  }
}
