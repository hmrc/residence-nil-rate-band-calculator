import java.time.LocalDate

import helpers.BaseComponentClass
import play.api.http.Status._
import play.api.libs.ws.WSResponse

class CaseStudy1SimpleCase extends BaseComponentClass {

  "The calculate route" should{
    "return a valid OK response" when{
      "following case study 1.1 - A simple case" in{
        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactory(
              dateOfDeath = LocalDate.of(2021, 1 ,1),
              valueOfEstate = 490000,
              propertyValue = 300000,
              chargeableEstateValue = 490000,
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

      "following case study 1.2 - Death occurs in an earlier tax year" in{
        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactory(
              dateOfDeath = LocalDate.of(2018, 1 ,1),
              valueOfEstate = 490000,
              propertyValue = 300000,
              chargeableEstateValue = 490000,
              percentagePassedToDirectDescendants = 100,
              valueBeingTransferred = 0
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 100000,
          applicableNilRateBandAmount = 100000,
          carryForwardAmount = 0,
          defaultAllowanceAmount = 100000,
          adjustedAllowanceAmount =100000
        )

        request.status shouldBe OK
        request.json shouldBe response
      }
    }
  }
}
