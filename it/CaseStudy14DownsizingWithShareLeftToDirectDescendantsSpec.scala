import org.joda.time.LocalDate
import java.time.{LocalDate => javaLocalDate}
import helpers.BaseComponentClass
import play.api.http.Status._
import play.api.libs.ws.WSResponse
import uk.gov.hmrc.residencenilratebandcalculator.models.{DownsizingDetails, PropertyValueAfterExemption}

class CaseStudy14DownsizingWithShareLeftToDirectDescendantsSpec extends BaseComponentClass{

  "The calculate route" should{
    "return a valid OK response" when{
      "following case study 14.1 - A simple case of downsizing with only part of the property left to descendants" in{
        val testDownsizingDetails = DownsizingDetails(
          datePropertyWasChanged = LocalDate.parse("2019-02-01"),
          valueAvailableWhenPropertyChanged = 0,
          valueOfChangedProperty = 400000,
          valueOfAssetsPassing = 150000
        )

        val testPropertyExemption = PropertyValueAfterExemption(
          value = 52500,
          inheritedValue = 52500
        )

        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactoryWithExemptionAndDownsizing(
              dateOfDeath = javaLocalDate.of(2020,9,1),
              valueOfEstate = 700000,
              propertyValue = 105000,
              chargeableEstateValue = 700000,
              percentagePassedToDirectDescendants = 50,
              valueBeingTransferred = 0,
              propertyExemptions = testPropertyExemption,
              downsizingDetails = testDownsizingDetails
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 122500,
          applicableNilRateBandAmount = 175000,
          carryForwardAmount = 52500,
          defaultAllowanceAmount = 175000,
          adjustedAllowanceAmount =175000
        )

        request.status shouldBe OK
        request.json shouldBe response
      }

      "following case study 14.2 - A simple case of downsizing with only part of the property left to descendants" in{
        val testDownsizingDetails = DownsizingDetails(
          datePropertyWasChanged = LocalDate.parse("2019-02-01"),
          valueAvailableWhenPropertyChanged = 0,
          valueOfChangedProperty = 400000,
          valueOfAssetsPassing = 20000
        )

        val testPropertyExemption = PropertyValueAfterExemption(
          value = 52500,
          inheritedValue = 52500
        )

        def request: WSResponse = ws.url(calculateUrl)
          .post(
            jsonHelper.jsonRequestFactoryWithExemptionAndDownsizing(
              dateOfDeath = javaLocalDate.of(2020,9,1),
              valueOfEstate = 700000,
              propertyValue = 105000,
              chargeableEstateValue = 700000,
              percentagePassedToDirectDescendants = 50,
              valueBeingTransferred = 0,
              propertyExemptions = testPropertyExemption,
              downsizingDetails = testDownsizingDetails
            )
          )

        val response = jsonHelper.jsonResponseFactory(
          residenceNilRateAmount = 72500,
          applicableNilRateBandAmount = 175000,
          carryForwardAmount = 102500,
          defaultAllowanceAmount = 175000,
          adjustedAllowanceAmount =175000
        )

        request.status shouldBe OK
        request.json shouldBe response
      }
    }
  }
}
