import java.time.LocalDate

import helpers.BaseComponentClass
import play.api.libs.ws.WSResponse
import play.api.http.Status._

class CaseStudy7SaleOfHomeSpec extends BaseComponentClass{

  "The calculate route" should{
    "return a valid OK response" when{
      "following case study 7.1 - A simple case" in{
        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactory(
              dateOfDeath = LocalDate.of(2019, 12 ,1),
              valueOfEstate = 900000,
              propertyValue = 500000,
              chargeableEstateValue = 900000,
              percentagePassedToDirectDescendants = 100,
              valueBeingTransferred = 0
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 150000,
          applicableNilRateBandAmount = 150000,
          carryForwardAmount = 0,
          defaultAllowanceAmount = 150000,
          adjustedAllowanceAmount =150000
        )

        request.status shouldBe OK
        request.json shouldBe response
      }
    }
  }
}
