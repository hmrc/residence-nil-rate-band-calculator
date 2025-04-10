/*
 * Copyright 2024 HM Revenue & Customs
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

package uk.gov.hmrc.residencenilratebandcalculator.models

import common.CommonPlaySpec
import java.time.LocalDate
import play.api.libs.json._

class CalculationInputTest extends CommonPlaySpec {

  "Calculation Input" must {
    "throw an exception when valueOfEstate is less than zero" in {
      val caught = intercept[IllegalArgumentException] {
        CalculationInput(LocalDate.now(), -1, 0, 0, 0, 0)
      }
      assert(caught.getMessage == "requirement failed: {\"valueOfEstate\" : \"error.expected.number.non_negative\"}")
    }

    "throw an exception when propertyValue is less than zero" in {
      val caught = intercept[IllegalArgumentException] {
        CalculationInput(LocalDate.now(), 0, 0, -1, 0, 0)
      }
      assert(caught.getMessage == "requirement failed: {\"propertyValue\" : \"error.expected.number.non_negative\"}")
    }

    "throw an exception when percentagePassedToDirectDescendants is less than zero" in {
      val caught = intercept[IllegalArgumentException] {
        CalculationInput(LocalDate.now(), 0, 0, 0, -1, 0)
      }
      assert(
        caught.getMessage == "requirement failed: {\"percentagePassedToDirectDescendants\" : \"error.expected.number.non_negative\"}"
      )
    }

    "throw an exception when percentagePassedToDirectDescendants is greater than one hundred" in {
      val caught = intercept[IllegalArgumentException] {
        CalculationInput(LocalDate.now(), 0, 0, 0, 101, 0)
      }
      assert(
        caught.getMessage == "requirement failed: {\"percentagePassedToDirectDescendants\" : \"error.expected.number.100_at_most\"}"
      )
    }

    "throw an exception when valueBeingTransferred is less than zero" in {
      val caught = intercept[IllegalArgumentException] {
        CalculationInput(LocalDate.now(), 0, 0, 0, 0, -1)
      }
      assert(
        caught.getMessage == "requirement failed: {\"valueBeingTransferred\" : \"error.expected.number.non_negative\"}"
      )
    }

    "throw an exception when propertyValueAfterExemption is present but its value is less than 0" in {
      val caught = intercept[IllegalArgumentException] {
        CalculationInput(LocalDate.now(), 0, 0, 0, 0, 0, Some(PropertyValueAfterExemption(-1, 0)))
      }
      assert(caught.getMessage == "requirement failed: {\"value\" : \"error.expected.number.non_negative\"}")
    }

    "throw an exception when propertyValueAfterExemption is present but its inheritedValue is less than 0" in {
      val caught = intercept[IllegalArgumentException] {
        CalculationInput(LocalDate.now(), 0, 0, 0, 0, 0, Some(PropertyValueAfterExemption(0, -1)))
      }
      assert(caught.getMessage == "requirement failed: {\"inheritedValue\" : \"error.expected.number.non_negative\"}")
    }

    "throw an exception when downsizingDetails are present but valueOfChangedProperty is less than 0" in {
      val caught = intercept[IllegalArgumentException] {
        CalculationInput(LocalDate.now(), 0, 0, 0, 0, 0, None, Some(DownsizingDetails(LocalDate.now(), -1, 0, 0)))
      }
      assert(
        caught.getMessage == "requirement failed: {\"valueOfChangedProperty\" : \"error.expected.number.non_negative\"}"
      )
    }

    "throw an exception when downsizingDetails are present but valueOfAssetsPassing is less than 0" in {
      val caught = intercept[IllegalArgumentException] {
        CalculationInput(LocalDate.now(), 0, 0, 0, 0, 0, None, Some(DownsizingDetails(LocalDate.now(), 0, -1, 0)))
      }
      assert(
        caught.getMessage == "requirement failed: {\"valueOfAssetsPassing\" : \"error.expected.number.non_negative\"}"
      )
    }

    "throw an exception when downsizingDetails are present but valueAvailableWhenPropertyChanged is less than 0" in {
      val caught = intercept[IllegalArgumentException] {
        CalculationInput(LocalDate.now(), 0, 0, 0, 0, 0, None, Some(DownsizingDetails(LocalDate.now(), 0, 0, -1)))
      }
      assert(
        caught.getMessage == "requirement failed: {\"valueAvailableWhenPropertyChanged\" : \"error.expected.number.non_negative\"}"
      )
    }

    "be constructable from JSON" in {
      val json = Json.parse("""
                              |{
                              | "dateOfDeath": "2018-01-01",
                              | "valueOfEstate": 0,
                              | "propertyValue": 1,
                              | "chargeableEstateValue": 2,
                              | "percentagePassedToDirectDescendants": 3,
                              | "valueBeingTransferred": 4
                              |}
        """.stripMargin)

      val input = Json.fromJson[CalculationInput](json).get

      assert(input.dateOfDeath == LocalDate.of(2018, 1, 1))
      assert(input.valueOfEstate == 0)
      assert(input.propertyValue == 1)
      assert(input.chargeableEstateValue == 2)
      assert(input.percentagePassedToDirectDescendants == 3)
      assert(input.valueBeingTransferred == 4)
    }

    "be constructable from JSON with property value after exemption present" in {
      val json = Json.parse("""
                              |{
                              | "dateOfDeath": "2018-01-01",
                              | "valueOfEstate": 0,
                              | "propertyValue": 1,
                              | "chargeableEstateValue": 2,
                              | "percentagePassedToDirectDescendants": 3,
                              | "valueBeingTransferred": 4,
                              | "propertyValueAfterExemption": {
                              |   "value": 5,
                              |   "inheritedValue": 6
                              | }
                              |}
        """.stripMargin)

      val input = Json.fromJson[CalculationInput](json).get

      assert(input.dateOfDeath == LocalDate.of(2018, 1, 1))
      assert(input.valueOfEstate == 0)
      assert(input.propertyValue == 1)
      assert(input.chargeableEstateValue == 2)
      assert(input.percentagePassedToDirectDescendants == 3)
      assert(input.valueBeingTransferred == 4)
      assert(input.propertyValueAfterExemption.contains(PropertyValueAfterExemption(5, 6)))
    }

    "be constructable from JSON with downsizing details present" in {
      val json = Json.parse("""
                              |{
                              | "dateOfDeath": "2018-01-01",
                              | "valueOfEstate": 0,
                              | "propertyValue": 1,
                              | "chargeableEstateValue": 2,
                              | "percentagePassedToDirectDescendants": 3,
                              | "valueBeingTransferred": 4,
                              | "downsizingDetails": {
                              |   "datePropertyWasChanged": "2017-01-01",
                              |   "valueOfChangedProperty": 5,
                              |   "valueOfAssetsPassing": 6,
                              |   "valueAvailableWhenPropertyChanged": 7
                              | }
                              |}
        """.stripMargin)

      val input = Json.fromJson[CalculationInput](json).get

      assert(input.dateOfDeath == LocalDate.of(2018, 1, 1))
      assert(input.valueOfEstate == 0)
      assert(input.propertyValue == 1)
      assert(input.chargeableEstateValue == 2)
      assert(input.percentagePassedToDirectDescendants == 3)
      assert(input.valueBeingTransferred == 4)
      assert(input.downsizingDetails.contains(DownsizingDetails(LocalDate.of(2017, 1, 1), 5, 6, 7)))
    }

    "fail to create case class when JSON does not match schema" in {
      val json = Json.parse("{}")

      val input: JsResult[CalculationInput] = Json.fromJson[CalculationInput](json)
      input match {
        case error: JsError =>
          assert(
            ((JsError.toJson(error) \ "obj.dateOfDeath") \ 0 \ "msg").as[Array[String]].head == "error.path.missing"
          )
          assert(
            ((JsError.toJson(error) \ "obj.valueOfEstate") \ 0 \ "msg").as[Array[String]].head == "error.path.missing"
          )
          assert(
            ((JsError.toJson(error) \ "obj.propertyValue") \ 0 \ "msg").as[Array[String]].head == "error.path.missing"
          )
          assert(
            ((JsError.toJson(error) \ "obj.chargeableEstateValue") \ 0 \ "msg")
              .as[Array[String]]
              .head == "error.path.missing"
          )
          assert(
            ((JsError.toJson(error) \ "obj.percentagePassedToDirectDescendants") \ 0 \ "msg")
              .as[Array[String]]
              .head == "error.path.missing"
          )
          assert(
            ((JsError.toJson(error) \ "obj.valueBeingTransferred") \ 0 \ "msg")
              .as[Array[String]]
              .head == "error.path.missing"
          )
        case _ => fail("Invalid JSON object construction succeeded")
      }
    }

    "fail to create case class when PropertyValueAfterExemption JSON does not match schema" in {
      val json = Json.parse("""
                              |{
                              | "dateOfDeath": "2018-01-01",
                              | "valueOfEstate": 0,
                              | "propertyValue": 1,
                              | "chargeableEstateValue": 2,
                              | "percentagePassedToDirectDescendants": 3,
                              | "valueBeingTransferred": 4,
                              | "propertyValueAfterExemption": {}
                              |}
        """.stripMargin)

      val input: JsResult[CalculationInput] = Json.fromJson[CalculationInput](json)
      input match {
        case error: JsError =>
          assert(
            ((JsError.toJson(error) \ "obj.propertyValueAfterExemption.value") \ 0 \ "msg")
              .as[Array[String]]
              .head == "error.path.missing"
          )
          assert(
            ((JsError.toJson(error) \ "obj.propertyValueAfterExemption.inheritedValue") \ 0 \ "msg")
              .as[Array[String]]
              .head == "error.path.missing"
          )
        case _ => fail("Invalid JSON object construction succeeded")
      }
    }

    "fail to create case class when DownsizingDetails JSON does not match schema" in {
      val json = Json.parse("""
                              |{
                              | "dateOfDeath": "2018-01-01",
                              | "valueOfEstate": 0,
                              | "propertyValue": 1,
                              | "chargeableEstateValue": 2,
                              | "percentagePassedToDirectDescendants": 3,
                              | "valueBeingTransferred": 4,
                              | "downsizingDetails": {}
                              |}
        """.stripMargin)

      val input: JsResult[CalculationInput] = Json.fromJson[CalculationInput](json)
      input match {
        case error: JsError =>
          assert(
            ((JsError.toJson(error) \ "obj.downsizingDetails.datePropertyWasChanged") \ 0 \ "msg")
              .as[Array[String]]
              .head == "error.path.missing"
          )
          assert(
            ((JsError.toJson(error) \ "obj.downsizingDetails.valueOfChangedProperty") \ 0 \ "msg")
              .as[Array[String]]
              .head == "error.path.missing"
          )
          assert(
            ((JsError.toJson(error) \ "obj.downsizingDetails.valueOfAssetsPassing") \ 0 \ "msg")
              .as[Array[String]]
              .head == "error.path.missing"
          )
          assert(
            ((JsError.toJson(error) \ "obj.downsizingDetails.valueAvailableWhenPropertyChanged") \ 0 \ "msg")
              .as[Array[String]]
              .head == "error.path.missing"
          )
        case _ => fail("Invalid JSON object construction succeeded")
      }
    }

    "be constructable from a valid JsValue" in {
      val json = Json.parse("""
                              |{
                              | "dateOfDeath": "2018-01-01",
                              | "valueOfEstate": 0,
                              | "propertyValue": 1,
                              | "chargeableEstateValue": 2,
                              | "percentagePassedToDirectDescendants": 3,
                              | "valueBeingTransferred": 4
                              |}
        """.stripMargin)

      val input = CalculationInput(json).toOption.get

      assert(input.dateOfDeath == LocalDate.of(2018, 1, 1))
      assert(input.valueOfEstate == 0)
      assert(input.propertyValue == 1)
      assert(input.chargeableEstateValue == 2)
      assert(input.percentagePassedToDirectDescendants == 3)
      assert(input.valueBeingTransferred == 4)
    }

    "be constructable from a valid JsValue with property value after exemption present" in {
      val json = Json.parse("""
                              |{
                              | "dateOfDeath": "2018-01-01",
                              | "valueOfEstate": 0,
                              | "propertyValue": 1,
                              | "chargeableEstateValue": 2,
                              | "percentagePassedToDirectDescendants": 3,
                              | "valueBeingTransferred": 4,
                              | "propertyValueAfterExemption": {
                              |   "value": 5,
                              |   "inheritedValue": 6
                              | }
                              |}
        """.stripMargin)

      val input = CalculationInput(json).toOption.get

      assert(input.dateOfDeath == LocalDate.of(2018, 1, 1))
      assert(input.valueOfEstate == 0)
      assert(input.propertyValue == 1)
      assert(input.chargeableEstateValue == 2)
      assert(input.percentagePassedToDirectDescendants == 3)
      assert(input.valueBeingTransferred == 4)
      assert(input.propertyValueAfterExemption.contains(PropertyValueAfterExemption(5, 6)))
    }

    "be constructable from a valid JsValue with downsizing details present" in {
      val json = Json.parse("""
                              |{
                              | "dateOfDeath": "2018-01-01",
                              | "valueOfEstate": 0,
                              | "propertyValue": 1,
                              | "chargeableEstateValue": 2,
                              | "percentagePassedToDirectDescendants": 3,
                              | "valueBeingTransferred": 4,
                              | "downsizingDetails": {
                              |   "datePropertyWasChanged": "2017-01-01",
                              |   "valueOfChangedProperty": 5,
                              |   "valueOfAssetsPassing": 6,
                              |   "valueAvailableWhenPropertyChanged": 7
                              | }
                              |}
        """.stripMargin)

      val input = CalculationInput(json).toOption.get

      assert(input.dateOfDeath == LocalDate.of(2018, 1, 1))
      assert(input.valueOfEstate == 0)
      assert(input.propertyValue == 1)
      assert(input.chargeableEstateValue == 2)
      assert(input.percentagePassedToDirectDescendants == 3)
      assert(input.valueBeingTransferred == 4)
      assert(input.downsizingDetails.contains(DownsizingDetails(LocalDate.of(2017, 1, 1), 5, 6, 7)))
    }

    "fail with suitable error messages when values are missing" in {
      val json = Json.parse("{}")

      val errors = CalculationInput(json).swap.toOption.get

      val expcetedErrors = Set(
        ("dateOfDeath", "error.path.missing"),
        ("valueOfEstate", "error.path.missing"),
        ("propertyValue", "error.path.missing"),
        ("chargeableEstateValue", "error.path.missing"),
        ("percentagePassedToDirectDescendants", "error.path.missing"),
        ("valueBeingTransferred", "error.path.missing")
      )

      assert(errors.toSet == expcetedErrors)
    }

    "fail with suitable error when numbers are passed as strings" in {
      val json = Json.parse("""
                              |{
                              | "dateOfDeath": "2018-01-01",
                              | "valueOfEstate": "0",
                              | "propertyValue": 0,
                              | "chargeableEstateValue": 0,
                              | "percentagePassedToDirectDescendants": 0,
                              | "valueBeingTransferred": 0
                              |}
        """.stripMargin)

      val errors = CalculationInput(json).swap.toOption.get

      val expectedErrors = Seq(("valueOfEstate", "error.expected.jsnumber"))

      assert(errors == expectedErrors)
    }

    "fail with a suitable error when invalid dates are passed" in {
      val json = Json.parse("""
                              |{
                              | "dateOfDeath": "",
                              | "valueOfEstate": 0,
                              | "propertyValue": 0,
                              | "chargeableEstateValue": 0,
                              | "percentagePassedToDirectDescendants": 0,
                              | "valueBeingTransferred": 0
                              |}
        """.stripMargin)

      val errors = CalculationInput(json).swap.toOption.get

      val expectedErrors = Seq(("dateOfDeath", "error.expected.date.isoformat"))

      assert(errors == expectedErrors)
    }

    "fail with a suitable error when numbers less than zero are passed" in {
      val json = Json.parse("""
                              |{
                              | "dateOfDeath": "2018-01-01",
                              | "valueOfEstate": -1,
                              | "propertyValue": 0,
                              | "chargeableEstateValue": 0,
                              | "percentagePassedToDirectDescendants": 0,
                              | "valueBeingTransferred": 0
                              |}
        """.stripMargin)

      val errors = CalculationInput(json).swap.toOption.get

      val expectedErrors = Seq(("valueOfEstate", "error.expected.number.non_negative"))

      assert(errors == expectedErrors)
    }

    "return propertyValueAfterExemption.inheritedValue as the propertyValueCloselyInherited when it is present" in {
      CalculationInput(
        LocalDate.now(),
        1,
        2,
        3,
        4,
        5,
        Some(PropertyValueAfterExemption(6, 7))
      ).propertyValuePassedToDirectDescendants shouldBe 7
    }

    "return (propertyValue * percentagePassedToDirectDescendants / 100) as the propertyValueCloselyInherited when propertyValueAfterExemption is not present" in {
      CalculationInput(
        LocalDate.now(),
        1,
        2,
        3,
        4,
        5
      ).propertyValuePassedToDirectDescendants shouldBe (3.0 * (4.0 / 100.0)).toInt
    }
  }

}
