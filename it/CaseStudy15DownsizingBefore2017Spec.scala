import org.joda.time.LocalDate
import java.time.{LocalDate => javaLocalDate}
import helpers.BaseComponentClass
import play.api.http.Status._
import play.api.libs.ws.WSResponse
import uk.gov.hmrc.residencenilratebandcalculator.models.{DownsizingDetails, PropertyValueAfterExemption}

class CaseStudy15DownsizingBefore2017Spec extends BaseComponentClass {

  "The calculate route" should{
    "return a valid OK response" when{
      "following case study 15.1 - A simple case of downsizing before 6 April 2017" in{
        val testDownsizingDetails = DownsizingDetails(
          datePropertyWasChanged = LocalDate.parse("2016-03-01"),
          valueAvailableWhenPropertyChanged = 0,
          valueOfChangedProperty = 150000,
          valueOfAssetsPassing = 80000
        )

        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactoryWithDownsizing(
              dateOfDeath = javaLocalDate.of(2019,12,1),
              valueOfEstate = 185000,
              propertyValue = 105000,
              chargeableEstateValue = 80000,
              percentagePassedToDirectDescendants = 0,
              valueBeingTransferred = 0,
              downsizingDetails = testDownsizingDetails
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 45000,
          applicableNilRateBandAmount = 150000,
          carryForwardAmount = 105000,
          defaultAllowanceAmount = 150000,
          adjustedAllowanceAmount =150000
        )

        request.status shouldBe OK
        request.json shouldBe response
      }

      "following case study 15.2 - A simple case of downsizing before 6 April 2017" in{
        val testDownsizingDetails = DownsizingDetails(
          datePropertyWasChanged = LocalDate.parse("2016-03-01"),
          valueAvailableWhenPropertyChanged = 0,
          valueOfChangedProperty = 150000,
          valueOfAssetsPassing = 10000
        )

        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactoryWithDownsizing(
              dateOfDeath = javaLocalDate.of(2019,12,1),
              valueOfEstate = 185000,
              propertyValue = 0,
              chargeableEstateValue = 80000,
              percentagePassedToDirectDescendants = 0,
              valueBeingTransferred = 0,
              downsizingDetails = testDownsizingDetails
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 10000,
          applicableNilRateBandAmount = 150000,
          carryForwardAmount = 140000,
          defaultAllowanceAmount = 150000,
          adjustedAllowanceAmount =150000
        )

        request.status shouldBe OK
        request.json shouldBe response
      }
    }
  }
}
