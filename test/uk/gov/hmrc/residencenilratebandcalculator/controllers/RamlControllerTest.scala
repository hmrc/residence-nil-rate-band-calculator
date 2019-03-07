/*
 * Copyright 2019 HM Revenue & Customs
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

package uk.gov.hmrc.residencenilratebandcalculator.controllers

import akka.stream.Materializer
import play.api.mvc.ControllerComponents
import play.api.test.FakeRequest
import play.api.test.Helpers._
import uk.gov.hmrc.play.test.{UnitSpec, WithFakeApplication}

class RamlControllerTest extends UnitSpec with WithFakeApplication {
  def injectedComps = fakeApplication.injector.instanceOf[ControllerComponents]

  "Raml Controller" must {
    implicit val mat = fakeApplication.injector.instanceOf[Materializer]
    val request = FakeRequest()

    "return a valid RAML file when requested" in {
      val result = new RamlController(injectedComps).getRaml("0.1")(request)
      status(result) shouldBe 200
      contentAsString(result) should include("#%RAML 1.0")
    }

    "return a valid JSON schema when requested" in {
      val result = new RamlController(injectedComps).getSchema("0.1", "deceaseds-estate.jsonschema")(request)
      status(result) shouldBe 200
      contentAsString(result) should include(""""$schema": "http://json-schema.org/draft-04/schema#"""")
    }
  }
}
