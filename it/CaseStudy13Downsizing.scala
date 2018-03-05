import org.joda.time.LocalDate
import java.time.{LocalDate => javaLocalDate}
import helpers.BaseComponentClass
import play.api.http.Status._
import play.api.libs.ws.WSResponse
import uk.gov.hmrc.residencenilratebandcalculator.models.DownsizingDetails

class CaseStudy13Downsizing extends BaseComponentClass{

  "The calculate route" should{
    "return a valid OK response" when{
      "following case study 13.1 - A simple case of downsizing" in{
        val testDownsizingDetails = DownsizingDetails(
          datePropertyWasChanged = LocalDate.parse("2018-05-01"),
          valueAvailableWhenPropertyChanged = 0,
          valueOfChangedProperty = 500000,
          valueOfAssetsPassing = 200000
        )

        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactoryWithDownsizing(
              dateOfDeath = javaLocalDate.of(2020,9,1),
              valueOfEstate = 305000,
              propertyValue = 105000,
              chargeableEstateValue = 305000,
              percentagePassedToDirectDescendants = 100,
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

      "following case study 13.2 - A simple case of downsizing" in{
        val testDownsizingDetails = DownsizingDetails(
          datePropertyWasChanged = LocalDate.parse("2018-05-01"),
          valueAvailableWhenPropertyChanged = 0,
          valueOfChangedProperty = 500000,
          valueOfAssetsPassing = 50000
        )

        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactoryWithDownsizing(
              dateOfDeath = javaLocalDate.of(2020,9,1),
              valueOfEstate = 305000,
              propertyValue = 105000,
              chargeableEstateValue = 305000,
              percentagePassedToDirectDescendants = 100,
              valueBeingTransferred = 0,
              downsizingDetails = testDownsizingDetails
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 155000,
          applicableNilRateBandAmount = 175000,
          carryForwardAmount = 20000,
          defaultAllowanceAmount = 175000,
          adjustedAllowanceAmount =175000
        )

        request.status shouldBe OK
        request.json shouldBe response
      }

    }
  }
}
