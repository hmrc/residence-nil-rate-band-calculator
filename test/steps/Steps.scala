/*
 * Copyright 2017 HM Revenue & Customs
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
import utils.{DataTableHelper, HttpConnector}
import scalaj.http._

import scala.collection.JavaConverters._

class Steps extends ScalaDsl with EN with Matchers {

  When("""^I POST these details to (.*)$""") { (endpoint: String, dataTable: DataTable) =>
    val json = DataTableHelper.convertToJsonString(dataTable)

    HttpConnector.post(endpoint, json)
  }

  When("""^I GET the nil rate band for (.*)$""") { (urlSlug: String) =>
    HttpConnector.get(s"nilrateband/$urlSlug")
  }

  When("""^I combine these details$""") { (dataTable: DataTable) =>
    Context.details = DataTableHelper.convertToJsValue(dataTable)
  }

  When("^(?:I combine )?these [P|p]roperty [V|v]alue [A|a]fter [E|e]xemption details$") { (dataTable: DataTable) =>
    val innerJson = DataTableHelper.convertToJsValue(dataTable)
    Context.details = Context.details + ("propertyValueAfterExemption" -> innerJson)
  }

  When("""^(?:I combine )?these [D|d]ownsizing [D|d]etails$""") { (dataTable: DataTable) =>
    val innerJson = DataTableHelper.convertToJsValue(dataTable)
    Context.details = Context.details + ("downsizingDetails" -> innerJson)
  }

  When("""^(?:I )?POST the details to (.*)$""") { (endpoint: String) =>
    val json = Context.details.toString

    HttpConnector.post(endpoint, json)
  }

  Then("""^I should get an? (.*) response$""") { (expectedResponseCode: String) =>
    HttpResponses.statusCodes(expectedResponseCode) shouldBe Context.responseCode
  }

  Then("""^the response body should be$""") { (dataTable: DataTable) =>
    val expectedItems = dataTable.asMap(classOf[String], classOf[String]).asScala

    for (item <- expectedItems) {
      val jsonNode = DataTableHelper.convertToJsonNode(item._1, item._2)
      Context.responseBodyAsMap should contain (jsonNode._1 -> jsonNode._2)
    }
  }

  Then("""^the band I get back should be (.*)$""") { (expectedBody: String) =>
    Context.responseBody shouldBe expectedBody
  }
}
