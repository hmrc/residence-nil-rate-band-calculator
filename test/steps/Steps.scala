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

package steps

import cucumber.api.DataTable
import cucumber.api.scala.{EN, ScalaDsl}
import org.scalatest.Matchers
import utils.DataTableHelper
import scalaj.http._

import scala.collection.JavaConverters._
import play.api.libs.json._

class Steps extends ScalaDsl with EN with Matchers {

  When("""^I POST these details to (.*)$""") { (endpoint: String, dataTable: DataTable) =>
    val json = DataTableHelper.convertToJsonString(dataTable)

    val response = Http(s"${Env.baseUrl}$endpoint").postData(json).header("content-type", "application/json").asString

    Context.responseCode = response.code
    Context.responseBody = response.body
  }

  Then("""^I should get an? (.*) response$""") { (expectedResponseCode: String) =>
    HttpResponses.statusCodes(expectedResponseCode) shouldBe Context.responseCode
  }

  Then("""^the response body should be$""") { (dataTable: DataTable) =>
    val expectedItems = dataTable.asMap(classOf[String], classOf[String]).asScala

    for (item <- expectedItems) {
      val jsonNode = DataTableHelper.convertToJsonNode(item._1, item._2)
      Context.responseBodyAsMap should contain (jsonNode._1 -> Json.toJson(jsonNode._2))
    }
  }
}
