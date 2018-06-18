import org.joda.time.LocalDate
import java.time.{LocalDate => javaLocalDate}
import helpers.BaseComponentClass
import play.api.http.Status._
import play.api.libs.ws.WSResponse
import uk.gov.hmrc.residencenilratebandcalculator.models.DownsizingDetails

class CaseStudy17DownsizingWithPropertyLowerThanAvailableRnrbSpec extends BaseComponentClass {

  "The calculate route" should{
    "return a valid OK response" when{
      "following case study 17.1 - A simple case of downsizing from a property which was worth less than the available RNRB" in{
        val testDownsizingDetails = DownsizingDetails(
          datePropertyWasChanged = LocalDate.parse("2019-05-01"),
          valueAvailableWhenPropertyChanged = 0,
          valueOfChangedProperty = 90000,
          valueOfAssetsPassing = 600000
        )

        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactoryWithDownsizing(
              dateOfDeath = javaLocalDate.of(2021,1,1),
              valueOfEstate = 600000,
              propertyValue = 0,
              chargeableEstateValue = 600000,
              percentagePassedToDirectDescendants = 0,
              valueBeingTransferred = 0,
              downsizingDetails = testDownsizingDetails
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 105000,
          applicableNilRateBandAmount = 175000,
          carryForwardAmount = 70000,
          defaultAllowanceAmount = 175000,
          adjustedAllowanceAmount =175000
        )

        request.status shouldBe OK
        request.json shouldBe response
      }

      "following case study 17.2 - A simple case of downsizing from a property which was worth less than the available RNRB" in{
        val testDownsizingDetails = DownsizingDetails(
          datePropertyWasChanged = LocalDate.parse("2019-05-01"),
          valueAvailableWhenPropertyChanged = 0,
          valueOfChangedProperty = 90000,
          valueOfAssetsPassing = 100000
        )

        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactoryWithDownsizing(
              dateOfDeath = javaLocalDate.of(2021,1,1),
              valueOfEstate = 100000,
              propertyValue = 0,
              chargeableEstateValue = 100000,
              percentagePassedToDirectDescendants = 0,
              valueBeingTransferred = 0,
              downsizingDetails = testDownsizingDetails
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