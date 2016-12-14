/*
 * Copyright 2016 HM Revenue & Customs
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

import org.joda.time.LocalDate
import play.api.libs.json._
import uk.gov.hmrc.play.test.UnitSpec

class CalculationInputTest extends UnitSpec {
  "Calculation Input" must {

    "throw an exception when grossEstateValue is less than zero" in {
      val caught = intercept[IllegalArgumentException] {
        CalculationInput(new LocalDate(), -1, 0, 0, 0)
      }
      assert(caught.getMessage == "requirement failed: {\"grossEstateValue\" : \"error.expected.number.non_negative\"}")
    }

    "throw an exception when propertyValue is less than zero" in {
      val caught = intercept[IllegalArgumentException] {
        CalculationInput(new LocalDate(), 0, -1, 0, 0)
      }
      assert(caught.getMessage == "requirement failed: {\"propertyValue\" : \"error.expected.number.non_negative\"}")
    }

    "throw an exception when percentageCloselyInherited is less than zero" in {
      val caught = intercept[IllegalArgumentException] {
        CalculationInput(new LocalDate(), 0, 0, 0, -1)
      }
      assert(caught.getMessage == "requirement failed: {\"percentageCloselyInherited\" : \"error.expected.number.non_negative\"}")
    }

    "throw an exception when percentageCloselyInherited is greater than one hundred" in {
      val caught = intercept[IllegalArgumentException] {
        CalculationInput(new LocalDate(), 0, 0, 0, 101)
      }
      assert(caught.getMessage == "requirement failed: {\"percentageCloselyInherited\" : \"error.expected.number.100_at_most\"}")
    }

    "be constructable from JSON" in {
      val json = Json.parse(
        """
          |{
          | "dateOfDeath": "2018-01-01",
          | "grossEstateValue": 0,
          | "propertyValue": 1,
          | "chargeableTransferAmount": 2,
          | "percentageCloselyInherited": 3
          |}
        """.stripMargin)

      val input = Json.fromJson[CalculationInput](json).get

      assert(input.dateOfDeath == new LocalDate(2018, 1, 1))
      assert(input.grossEstateValue == 0)
      assert(input.propertyValue == 1)
      assert(input.chargeableTransferAmount == 2)
      assert(input.percentageCloselyInherited == 3)
    }

    "fail to create case class when JSON does not match schema" in {
      val json = Json.parse("{}")

      val input: JsResult[CalculationInput] = Json.fromJson[CalculationInput](json)
      input match {
        case error: JsError => {
          assert(((JsError.toJson(error) \ "obj.dateOfDeath") \ 0 \ "msg").as[Array[String]].head == "error.path.missing")
          assert(((JsError.toJson(error) \ "obj.grossEstateValue") \ 0 \ "msg").as[Array[String]].head == "error.path.missing")
          assert(((JsError.toJson(error) \ "obj.propertyValue") \ 0 \ "msg").as[Array[String]].head == "error.path.missing")
          assert(((JsError.toJson(error) \ "obj.chargeableTransferAmount") \ 0 \ "msg").as[Array[String]].head == "error.path.missing")
          assert(((JsError.toJson(error) \ "obj.percentageCloselyInherited") \ 0 \ "msg").as[Array[String]].head == "error.path.missing")
        }
        case _ => fail("Invalid JSON object construction succeeded")
      }
    }

    "be constructable from a valid JsValue" in {
      val json = Json.parse(
        """
          |{
          | "dateOfDeath": "2018-01-01",
          | "grossEstateValue": 0,
          | "propertyValue": 1,
          | "chargeableTransferAmount": 2,
          | "percentageCloselyInherited": 3
          |}
        """.stripMargin)

      val input = CalculationInput(json).right.get

      assert(input.dateOfDeath == new LocalDate(2018, 1, 1))
      assert(input.grossEstateValue == 0)
      assert(input.propertyValue == 1)
      assert(input.chargeableTransferAmount == 2)
      assert(input.percentageCloselyInherited == 3)
    }

    "fail with suitable error messages when values are missing" in {
      val json = Json.parse("{}")

      val errors = CalculationInput(json).left.get

      val expcetedErrors = Set(
        ("dateOfDeath", "error.path.missing"),
        ("grossEstateValue", "error.path.missing"),
        ("propertyValue", "error.path.missing"),
        ("chargeableTransferAmount", "error.path.missing")),
        ("percentageCloselyInherited", "error.path.missing"))

      assert(errors.toSet == expcetedErrors)
    }

    "fail with suitable error when numbers are passed as strings" in {
      val json = Json.parse(
        """
          |{
          | "dateOfDeath": "2018-01-01",
          | "grossEstateValue": "0",
          | "propertyValue": 0,
          | "chargeableTransferAmount": 0
          |}
        """.stripMargin)

      val errors = CalculationInput(json).left.get

      val expectedErrors = Seq(("grossEstateValue", "error.expected.jsnumber"))

      assert(errors == expectedErrors)
    }

    "fail with a suitable error when invalid dates are passed" in {
      val json = Json.parse(
        """
          |{
          | "dateOfDeath": "",
          | "grossEstateValue": 0,
          | "propertyValue": 0,
          | "chargeableTransferAmount": 0
          |}
        """.stripMargin)

      val errors = CalculationInput(json).left.get

      val expectedErrors = Seq(("dateOfDeath", "error.expected.jodadate.format"))

      assert(errors == expectedErrors)
    }

    "fail with a suitable error when numbers less than zero are passed" in {
      val json = Json.parse(
        """
          |{
          | "dateOfDeath": "2018-01-01",
          | "grossEstateValue": -1,
          | "propertyValue": 0,
          | "chargeableTransferAmount": 0
          |}
        """.stripMargin)

      val errors = CalculationInput(json).left.get

      val expectedErrors = Seq(("grossEstateValue", "error.expected.number.non_negative"))

      assert(errors == expectedErrors)
    }
  }
}
