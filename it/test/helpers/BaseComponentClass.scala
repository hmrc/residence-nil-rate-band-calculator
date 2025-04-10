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

package helpers

import org.scalatest.OptionValues
import org.scalatest.matchers.should.Matchers
import org.scalatest.wordspec.AnyWordSpecLike
import org.scalatestplus.mockito.MockitoSugar
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.{Application, Environment}
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.ws.WSClient
import uk.gov.hmrc.residencenilratebandcalculator.models.Calculator

class BaseComponentClass
    extends AnyWordSpecLike
    with Matchers
    with OptionValues
    with GuiceOneServerPerSuite
    with MockitoSugar {

  override implicit lazy val app: Application = new GuiceApplicationBuilder()
    .build()

  val calculateUrl                  = s"http://localhost:$port/residence-nil-rate-band-calculator/calculate"
  def nilRateBandUrl(date: String)  = s"http://localhost:$port/residence-nil-rate-band-calculator/nilrateband/$date"
  val jsonHelper: jsonHelperFactory = jsonHelperFactory()
  lazy val ws: WSClient             = app.injector.instanceOf[WSClient]

  val env: Environment = mock[Environment]
  val calculator       = new Calculator(env)

}
