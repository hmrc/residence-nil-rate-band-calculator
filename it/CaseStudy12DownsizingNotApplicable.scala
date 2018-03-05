import org.joda.time.LocalDate
import java.time.{LocalDate => javaLocalDate}
import helpers.BaseComponentClass
import play.api.http.Status._
import play.api.libs.ws.WSResponse
import uk.gov.hmrc.residencenilratebandcalculator.models.DownsizingDetails

class CaseStudy12DownsizingNotApplicable extends BaseComponentClass{

  "The calculate route" should{
    "return a valid OK response" when{
      "following case study 12.1 - A simple case where downsizing is not due" in{
        val testDownsizingDetails = DownsizingDetails(
          datePropertyWasChanged = LocalDate.parse("2018-10-01"),
          valueAvailableWhenPropertyChanged = 0,
          valueOfChangedProperty = 450000,
          valueOfAssetsPassing = 500000
        )

        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactoryWithDownsizing(
              dateOfDeath = javaLocalDate.of(2020,8,1),
              valueOfEstate = 700000,
              propertyValue = 200000,
              chargeableEstateValue = 700000,
              percentagePassedToDirectDescendants = 0,
              valueBeingTransferred = 0,
              downsizingDetails = testDownsizingDetails
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 0,
          applicableNilRateBandAmount = 175000,
          carryForwardAmount = 175000,
          defaultAllowanceAmount = 175000,
          adjustedAllowanceAmount =175000
        )

        request.status shouldBe OK
        request.json shouldBe response
      }

    }
  }
}
