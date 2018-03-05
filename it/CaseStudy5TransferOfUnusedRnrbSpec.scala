import java.time.LocalDate

import helpers.BaseComponentClass
import play.api.http.Status._
import play.api.libs.ws.WSResponse

class CaseStudy5TransferOfUnusedRnrbSpec extends BaseComponentClass {

  "The calculate route" should{
    "return a valid OK response" when{
      "following case study 5.1 - A simple case" in{
        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactory(
              dateOfDeath = LocalDate.of(2019, 7 ,30),
              valueOfEstate = 1000000,
              propertyValue = 400000,
              chargeableEstateValue = 1000000,
              percentagePassedToDirectDescendants = 100,
              valueBeingTransferred = 150000
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 300000,
          applicableNilRateBandAmount = 150000,
          carryForwardAmount = 0,
          defaultAllowanceAmount = 300000,
          adjustedAllowanceAmount =300000
        )

        request.status shouldBe OK
        request.json shouldBe response
      }

      "following case study 5.2 - Property worth less than the RNRB + unused allowance" in{
        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactory(
              dateOfDeath = LocalDate.of(2019, 7 ,30),
              valueOfEstate = 750000,
              propertyValue = 250000,
              chargeableEstateValue = 750000,
              percentagePassedToDirectDescendants = 100,
              valueBeingTransferred = 150000
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 250000,
          applicableNilRateBandAmount = 150000,
          carryForwardAmount = 50000,
          defaultAllowanceAmount = 300000,
          adjustedAllowanceAmount = 300000
        )

        request.status shouldBe OK
        request.json shouldBe response
      }
    }
  }
}
