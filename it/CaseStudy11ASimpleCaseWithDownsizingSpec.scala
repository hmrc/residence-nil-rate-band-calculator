import org.joda.time.LocalDate
import java.time.{LocalDate => javaLocalDate}
import helpers.BaseComponentClass
import play.api.http.Status._
import play.api.libs.ws.WSResponse
import uk.gov.hmrc.residencenilratebandcalculator.models.DownsizingDetails

class CaseStudy11ASimpleCaseWithDownsizingSpec extends BaseComponentClass{

  "The calculate route" should{
    "return a valid OK response" when{
      "following case study 11.1 - A simple case" in{
        val testDownsizingDetails = DownsizingDetails(
          datePropertyWasChanged = LocalDate.parse("2018-06-1"),
          valueAvailableWhenPropertyChanged = 125000,
          valueOfChangedProperty = 195000,
          valueOfAssetsPassing = 850000
        )

        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactoryWithDownsizing(
              dateOfDeath = javaLocalDate.of(2018,5,1),
              valueOfEstate = 2100000,
              propertyValue = 450000,
              chargeableEstateValue = 2100000,
              percentagePassedToDirectDescendants = 0,
              valueBeingTransferred = 0,
              downsizingDetails = testDownsizingDetails
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

    }
  }
}
