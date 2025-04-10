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

package uk.gov.hmrc.residencenilratebandcalculator.converters

import common.{CommonPlaySpec, WithCommonFakeApplication}
import play.api.i18n.{Messages, MessagesApi}
import play.api.libs.json.{JsNumber, JsString}
import play.api.mvc.AnyContentAsEmpty
import play.api.test.FakeRequest
import play.api.test.Helpers._

class HttpErrorResponseTest extends CommonPlaySpec with WithCommonFakeApplication {

  implicit val fakeRequest: FakeRequest[AnyContentAsEmpty.type] = FakeRequest()
  implicit val messages: Messages = fakeApplication.injector.instanceOf[MessagesApi].preferred(fakeRequest)

  "Http Error Response" must {
    "create suitable errors" in {
      val failureMessageKey = "error.json_parsing_failure"
      val key               = "key"
      val errorMessageKey   = "error.path.missing"

      val result = HttpErrorResponse(BAD_REQUEST, failureMessageKey, Seq((key, errorMessageKey)))

      (result \ "statusCode").as[JsNumber].value shouldBe BAD_REQUEST
      (result \ "message").as[JsString].value shouldBe messages(failureMessageKey)
      (result \ "errors" \ key).as[JsString].value shouldBe messages(errorMessageKey)
    }
  }

}
