/*
 * Copyright 2021 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
