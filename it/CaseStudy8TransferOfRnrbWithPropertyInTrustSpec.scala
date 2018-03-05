import java.time.LocalDate

import helpers.BaseComponentClass
import play.api.http.Status._
import play.api.libs.ws.WSResponse

class CaseStudy8TransferOfRnrbWithPropertyInTrustSpec extends BaseComponentClass{

  "The calculate route" should{
    "return a valid OK response" when{
      "following case study 8.1 - A simple case" in{
        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactory(
              dateOfDeath = LocalDate.of(2020, 12 ,1),
              valueOfEstate = 1200000,
              propertyValue = 400000,
              chargeableEstateValue = 1250000,
              percentagePassedToDirectDescendants = 100,
              valueBeingTransferred = 175000
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 350000,
          applicableNilRateBandAmount = 175000,
          carryForwardAmount = 0,
          defaultAllowanceAmount = 350000,
          adjustedAllowanceAmount =350000
        )

        request.status shouldBe OK
        request.json shouldBe response
      }
    }
  }
}
