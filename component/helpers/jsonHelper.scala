package helpers

import java.time.LocalDate

import play.api.libs.json.{JsValue, Json}

case class jsonHelper() {

  def jsonRequestFactory(dateOfDeath: LocalDate,
                         valueOfEstate: Int,
                         propertyValue: Int,
                         chargeableEstateValue: Int,
                         percentagePassedToDirectDescendants: Int,
                         valueBeingTransferred: Int
                        ): JsValue ={
    Json.parse(
      s"""
        |{
        | "dateOfDeath": ${dateOfDeath.toString},
        | "valueOfEstate": $valueOfEstate,
        | "propertyValue": $propertyValue,
        | "chargeableEstateValue": $chargeableEstateValue,
        | "percentagePassedToDirectDescendants": $percentagePassedToDirectDescendants,
        | "valueBeingTransferred": $valueBeingTransferred
        |}
      """.stripMargin)
  }

  def jsonResultFactory(test: String): JsValue ={
    Json.parse(
      s"""
         |{
         |}
      """.stripMargin)
  }
}
