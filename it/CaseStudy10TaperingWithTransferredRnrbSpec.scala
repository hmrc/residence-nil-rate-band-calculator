import java.time.LocalDate

import helpers.BaseComponentClass
import play.api.http.Status._
import play.api.libs.ws.WSResponse

class CaseStudy10TaperingWithTransferredRnrbSpec extends BaseComponentClass {

  "The calculate route" should{
    "return a valid OK response" when{
      "following case study 10.1 - A simple case" in{
        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactory(
              dateOfDeath = LocalDate.of(2018,5,1),
              valueOfEstate = 2100000,
              propertyValue = 4500010,
              chargeableEstateValue = 2100000,
              percentagePassedToDirectDescendants = 0,
              valueBeingTransferred = 0
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 0,
          applicableNilRateBandAmount = 125000,
          carryForwardAmount = 75000,
          defaultAllowanceAmount = 125000,
          adjustedAllowanceAmount =75000
        )

        request.status shouldBe OK
        request.json shouldBe response
      }

      "following case study 10.2" in{
        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactory(
              dateOfDeath = LocalDate.of(2021,3,25),
              valueOfEstate = 1800000,
              propertyValue = 500000,
              chargeableEstateValue = 1800000,
              percentagePassedToDirectDescendants = 100,
              valueBeingTransferred = 105000
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 280000,
          applicableNilRateBandAmount = 175000,
          carryForwardAmount = 0,
          defaultAllowanceAmount = 280000,
          adjustedAllowanceAmount =280000
        )

        request.status shouldBe OK
        request.json shouldBe response
      }
    }
  }
}
