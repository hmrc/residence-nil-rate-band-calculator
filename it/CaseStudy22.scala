import org.joda.time.LocalDate
import java.time.{LocalDate => javaLocalDate}
import helpers.BaseComponentClass
import play.api.http.Status._
import play.api.libs.ws.WSResponse
import uk.gov.hmrc.residencenilratebandcalculator.models.DownsizingDetails

class CaseStudy22 extends BaseComponentClass{

  "The calculate route" should{
    "return a valid OK response" when{
      "following case study 22.1 - downsizing and leaving no property" in{
        val testDownsizingDetails = DownsizingDetails(
          datePropertyWasChanged = LocalDate.parse("2020-05-01"),
          valueAvailableWhenPropertyChanged = 0,
          valueOfChangedProperty = 200000,
          valueOfAssetsPassing = 200000
        )

        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactoryWithDownsizing(
              dateOfDeath = javaLocalDate.of(2020,8,1),
              valueOfEstate = 1500000,
              propertyValue = 0,
              chargeableEstateValue = 1500000,
              percentagePassedToDirectDescendants = 0,
              valueBeingTransferred = 0,
              downsizingDetails = testDownsizingDetails
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
    }
  }
}
