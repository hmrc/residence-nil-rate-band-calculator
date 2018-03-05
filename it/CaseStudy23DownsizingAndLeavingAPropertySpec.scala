import org.joda.time.LocalDate
import java.time.{LocalDate => javaLocalDate}
import helpers.BaseComponentClass
import play.api.http.Status._
import play.api.libs.ws.WSResponse
import uk.gov.hmrc.residencenilratebandcalculator.models.DownsizingDetails

class CaseStudy23DownsizingAndLeavingAPropertySpec extends BaseComponentClass{

  "The calculate route" should{
    "return a valid OK response" when{
      "following case study 23.1 - downsizing and leaving a property with Value Being Transferred" in{
        val testDownsizingDetails = DownsizingDetails(
          datePropertyWasChanged = LocalDate.parse("2018-10-01"),
          valueAvailableWhenPropertyChanged = 125000,
          valueOfChangedProperty = 500000,
          valueOfAssetsPassing = 710000
        )

        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactoryWithDownsizing(
              dateOfDeath = javaLocalDate.of(2020,5,1),
              valueOfEstate = 800000,
              propertyValue = 90000,
              chargeableEstateValue = 800000,
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
