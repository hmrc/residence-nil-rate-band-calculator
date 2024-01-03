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

import helpers.BaseComponentClass
import play.api.test.Helpers._
import play.api.libs.ws.WSResponse
import scala.concurrent.Future

class NilRateBandSpec extends BaseComponentClass{

  "The calculate route" should{
    "return a valid OK response" when{
      "the correct value is given for 5th April 2017" in{
        def request: Future[WSResponse] = ws.url(nilRateBandUrl("2017-04-05")).get()

        await(request).status shouldBe OK
        await(request).body shouldBe "100000"
      }

      "the correct value is given for 6th April 2017" in{
        def request: Future[WSResponse] = ws.url(nilRateBandUrl("2017-04-06")).get()

        await(request).status shouldBe OK
        await(request).body shouldBe "100000"
      }

      "the correct value is given for 1st Jan 2021" in{
        def request: Future[WSResponse] = ws.url(nilRateBandUrl("2021-01-01")).get()

        await(request).status shouldBe OK
        await(request).body shouldBe "175000"
      }
    }
  }

}
