/*
 * Copyright 2025 HM Revenue & Customs
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

import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpec
import play.api.libs.json.{Json, Reads, Writes}

class CalculationResultTest extends AnyWordSpec with Matchers {

  "CalculationResult" should {
    "serialize and deserialize correctly" in {
      val calculationResultFormat = CalculationResult.formats
      val calculationResultReads  = calculationResultFormat: Reads[CalculationResult]
      val calculationResultWrites = calculationResultFormat: Writes[CalculationResult]

      val result = CalculationResult(
        residenceNilRateAmount = 150000,
        applicableNilRateBandAmount = 175000,
        carryForwardAmount = 25000,
        defaultAllowanceAmount = 100000,
        adjustedAllowanceAmount = 125000
      )

      val x = Json.toJson(result)(calculationResultWrites)
      assert(Json.parse(x.toString()).as[CalculationResult](calculationResultReads) == result)
    }

    "fail to deserialize when required fields are missing" in {
      val invalidJson = Json.parse(
        """
          |{
          |  "residenceNilRateAmount": 150000
          |}
        """.stripMargin
      )
      invalidJson.validate[CalculationResult] shouldBe a[play.api.libs.json.JsError]
    }

    "fail to deserialize when fields are of the wrong type" in {
      val invalidJson = Json.parse(
        """
          |{
          |  "residenceNilRateAmount": "notAnInt",
          |  "applicableNilRateBandAmount": 175000,
          |  "carryForwardAmount": 25000,
          |  "defaultAllowanceAmount": 100000,
          |  "adjustedAllowanceAmount": 125000
          |}
        """.stripMargin
      )
      invalidJson.validate[CalculationResult] shouldBe a[play.api.libs.json.JsError]
    }

    "handle edge integer values" in {
      val result = CalculationResult(
        residenceNilRateAmount = Int.MaxValue,
        applicableNilRateBandAmount = Int.MinValue,
        carryForwardAmount = 0,
        defaultAllowanceAmount = Int.MaxValue,
        adjustedAllowanceAmount = Int.MinValue
      )
      val json = Json.toJson(result)
      json.validate[CalculationResult].get shouldBe result
    }
  }

}
