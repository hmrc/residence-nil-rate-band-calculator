package helpers

import java.time.LocalDate

import play.api.libs.json.{JsValue, Json}

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
