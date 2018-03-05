import org.joda.time.LocalDate
import java.time.{LocalDate => javaLocalDate}
import helpers.BaseComponentClass
import play.api.http.Status._
import play.api.libs.ws.WSResponse
import uk.gov.hmrc.residencenilratebandcalculator.models.DownsizingDetails

class CaseStudy16DownsizingWithNoHomeInEstate extends BaseComponentClass {

  "The calculate route" should{
    "return a valid OK response" when{
      "following case study 16.1 - A simple case of downsizing with no property remaining in the estate" in{
        val testDownsizingDetails = DownsizingDetails(
          datePropertyWasChanged = LocalDate.parse("2018-10-01"),
          valueAvailableWhenPropertyChanged = 0,
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

      "following case study 16.2 - A simple case of downsizing with no property remaining in the estate" in{
        val testDownsizingDetails = DownsizingDetails(
          datePropertyWasChanged = LocalDate.parse("2018-10-01"),
          valueAvailableWhenPropertyChanged = 0,
          valueOfChangedProperty = 285000,
          valueOfAssetsPassing = 100000
        )

        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactoryWithDownsizing(
              dateOfDeath = javaLocalDate.of(2021,3,1),
              valueOfEstate = 500000,
              propertyValue = 0,
              chargeableEstateValue = 500000,
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
