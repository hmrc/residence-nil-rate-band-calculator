import org.joda.time.LocalDate
import java.time.{LocalDate => javaLocalDate}
import helpers.BaseComponentClass
import play.api.http.Status._
import play.api.libs.ws.WSResponse
import uk.gov.hmrc.residencenilratebandcalculator.models.DownsizingDetails

class CaseStudy19DownsizingAndLeavingAPropertySpec extends BaseComponentClass {

  "The calculate route" should{
    "return a valid OK response" when{
      "following case study 19.1 - A simple case of downsizing and leaving a property with Value Being Transferred" in{
        val testDownsizingDetails = DownsizingDetails(
          datePropertyWasChanged = LocalDate.parse("2015-09-01"),
          valueAvailableWhenPropertyChanged = 0,
          valueOfChangedProperty = 300000,
          valueOfAssetsPassing = 425000
        )

        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactoryWithDownsizing(
              dateOfDeath = javaLocalDate.of(2020,5,1),
              valueOfEstate = 635000,
              propertyValue = 210000,
              chargeableEstateValue = 635000,
              percentagePassedToDirectDescendants = 100,
              valueBeingTransferred = 175000,
              downsizingDetails = testDownsizingDetails
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
