import org.joda.time.LocalDate
import java.time.{LocalDate => javaLocalDate}
import helpers.BaseComponentClass
import play.api.http.Status._
import play.api.libs.ws.WSResponse
import uk.gov.hmrc.residencenilratebandcalculator.models.DownsizingDetails

class CaseStudy18DownsizingWithTransferredRnrbSpec extends BaseComponentClass {

  "The calculate route" should{
    "return a valid OK response" when{
      "following case study 18.1 - A simple case of downsizing from a property which was worth less than the available RNRB" in{
        val testDownsizingDetails = DownsizingDetails(
          datePropertyWasChanged = LocalDate.parse("2018-10-01"),
          valueAvailableWhenPropertyChanged = 125000,
          valueOfChangedProperty = 285000,
          valueOfAssetsPassing = 250000
        )

        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactoryWithDownsizing(
              dateOfDeath = javaLocalDate.of(2021,3,1),
              valueOfEstate = 500000,
              propertyValue = 0,
              chargeableEstateValue = 500000,
              percentagePassedToDirectDescendants = 0,
              valueBeingTransferred = 175000,
              downsizingDetails = testDownsizingDetails
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 250000,
          applicableNilRateBandAmount = 175000,
          carryForwardAmount = 100000,
          defaultAllowanceAmount = 350000,
          adjustedAllowanceAmount = 350000
        )

        request.status shouldBe OK
        request.json shouldBe response
      }
    }
  }

}
