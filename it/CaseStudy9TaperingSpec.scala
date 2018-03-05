import java.time.LocalDate

import helpers.BaseComponentClass
import play.api.http.Status._
import play.api.libs.ws.WSResponse

class CaseStudy9TaperingSpec extends BaseComponentClass {

  "The calculate route" should{
    "return a valid OK response" when{
      "following case study 9.1 - A simple case" in{
        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactory(
              dateOfDeath = LocalDate.of(2018, 12 ,1),
              valueOfEstate = 2100000,
              propertyValue = 450000,
              chargeableEstateValue = 2100000,
              percentagePassedToDirectDescendants = 100,
              valueBeingTransferred = 0
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 75000,
          applicableNilRateBandAmount = 125000,
          carryForwardAmount = 0,
          defaultAllowanceAmount = 125000,
          adjustedAllowanceAmount =75000
        )

        request.status shouldBe OK
        request.json shouldBe response
      }
    }
  }
}
