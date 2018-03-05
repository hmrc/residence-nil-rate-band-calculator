import java.time.{LocalDate => javaLocalDate}
import helpers.BaseComponentClass
import play.api.http.Status._
import play.api.libs.ws.WSResponse
import uk.gov.hmrc.residencenilratebandcalculator.models.PropertyValueAfterExemption

class PropertyExemptions extends BaseComponentClass {

  "The calculate route" should{
    "return a valid OK response" when{
      "Property is partially exempt, and the remaining part is lower than the applicable nil-rate band" in{
        val testPropertyExemption = PropertyValueAfterExemption(
          value = 100000,
          inheritedValue = 100000
        )

        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactoryWithExemption(
              dateOfDeath = javaLocalDate.of(2021,1,1),
              valueOfEstate = 490000,
              propertyValue = 300000,
              chargeableEstateValue = 490000,
              percentagePassedToDirectDescendants = 100,
              valueBeingTransferred = 0,
              propertyExemptions = testPropertyExemption
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
    }
  }
}
