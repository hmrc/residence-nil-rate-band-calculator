package helpers

import java.time.LocalDate

import play.api.libs.json.{JsValue, Json}
import uk.gov.hmrc.residencenilratebandcalculator.models.{DownsizingDetails, PropertyValueAfterExemption}

case class jsonHelperFactory() {

  def jsonRequestFactory(dateOfDeath: LocalDate,
                         chargeableEstateValue: Double,
                         valueOfEstate: Double,
                         propertyValue: Double,
                         percentagePassedToDirectDescendants: Double,
                         valueBeingTransferred: Double
                        ): JsValue ={
    Json.parse(
      s"""
        |{
        | "dateOfDeath": "${dateOfDeath.toString}",
        | "valueOfEstate": $valueOfEstate,
        | "propertyValue": $propertyValue,
        | "chargeableEstateValue": $chargeableEstateValue,
        | "percentagePassedToDirectDescendants": $percentagePassedToDirectDescendants,
        | "valueBeingTransferred": $valueBeingTransferred
        |}
      """.stripMargin)
  }

  def jsonRequestFactoryWithExemption(dateOfDeath: LocalDate,
                                                   chargeableEstateValue: Double,
                                                   valueOfEstate: Double,
                                                   propertyValue: Double,
                                                   percentagePassedToDirectDescendants: Double,
                                                   valueBeingTransferred: Double,
                                                   propertyExemptions: PropertyValueAfterExemption
                                                  ): JsValue ={
    Json.parse(
      s"""
         |{
         | "dateOfDeath": "${dateOfDeath.toString}",
         | "valueOfEstate": $valueOfEstate,
         | "propertyValue": $propertyValue,
         | "chargeableEstateValue": $chargeableEstateValue,
         | "percentagePassedToDirectDescendants": $percentagePassedToDirectDescendants,
         | "valueBeingTransferred": $valueBeingTransferred,
         | "propertyValueAfterExemption" : {
         |     "value" : ${propertyExemptions.value},
         |     "inheritedValue" :${propertyExemptions.inheritedValue}
         |   }
         |}
      """.stripMargin)
  }


  def jsonRequestFactoryWithExemptionAndDownsizing(dateOfDeath: LocalDate,
                         chargeableEstateValue: Double,
                         valueOfEstate: Double,
                         propertyValue: Double,
                         percentagePassedToDirectDescendants: Double,
                         valueBeingTransferred: Double,
                         propertyExemptions: PropertyValueAfterExemption,
                         downsizingDetails: DownsizingDetails
                        ): JsValue ={
    Json.parse(
      s"""
         |{
         | "dateOfDeath": "${dateOfDeath.toString}",
         | "valueOfEstate": $valueOfEstate,
         | "propertyValue": $propertyValue,
         | "chargeableEstateValue": $chargeableEstateValue,
         | "percentagePassedToDirectDescendants": $percentagePassedToDirectDescendants,
         | "valueBeingTransferred": $valueBeingTransferred,
         | "propertyValueAfterExemption" : {
         |     "value" : ${propertyExemptions.value},
         |     "inheritedValue" :${propertyExemptions.inheritedValue}
         |   },
         | "downsizingDetails" : {
         |     "datePropertyWasChanged" : "${downsizingDetails.datePropertyWasChanged}",
         |     "valueOfChangedProperty" : ${downsizingDetails.valueOfChangedProperty},
         |     "valueOfAssetsPassing"   : ${downsizingDetails.valueOfAssetsPassing},
         |     "valueAvailableWhenPropertyChanged" : ${downsizingDetails.valueAvailableWhenPropertyChanged}
         |   }
         |}
      """.stripMargin)
  }


  def jsonRequestFactoryWithDownsizing(dateOfDeath: LocalDate,
                                       chargeableEstateValue: Double,
                                       valueOfEstate: Double,
                                       propertyValue: Double,
                                       percentagePassedToDirectDescendants: Double,
                                       valueBeingTransferred: Double,
                                       downsizingDetails: DownsizingDetails
                                      ): JsValue ={
    Json.parse(
      s"""
         |{
         | "dateOfDeath": "${dateOfDeath.toString}",
         | "valueOfEstate": $valueOfEstate,
         | "propertyValue": $propertyValue,
         | "chargeableEstateValue": $chargeableEstateValue,
         | "percentagePassedToDirectDescendants": $percentagePassedToDirectDescendants,
         | "valueBeingTransferred": $valueBeingTransferred,
         | "downsizingDetails" : {
         |     "datePropertyWasChanged" : "${downsizingDetails.datePropertyWasChanged}",
         |     "valueOfChangedProperty" : ${downsizingDetails.valueOfChangedProperty},
         |     "valueOfAssetsPassing"   : ${downsizingDetails.valueOfAssetsPassing},
         |     "valueAvailableWhenPropertyChanged" : ${downsizingDetails.valueAvailableWhenPropertyChanged}
         |   }
         |}
      """.stripMargin)
  }


  def jsonResponseFactory(residenceNilRateAmount: Double,
                          applicableNilRateBandAmount : Double,
                          carryForwardAmount: Double,
                          defaultAllowanceAmount: Double,
                          adjustedAllowanceAmount: Double
                         ): JsValue ={
    Json.parse(
      s"""
         |{
         | "residenceNilRateAmount": $residenceNilRateAmount,
         | "applicableNilRateBandAmount": $applicableNilRateBandAmount,
         | "carryForwardAmount": $carryForwardAmount,
         | "defaultAllowanceAmount": $defaultAllowanceAmount,
         | "adjustedAllowanceAmount": $adjustedAllowanceAmount
         |}
      """.stripMargin)
  }
}
