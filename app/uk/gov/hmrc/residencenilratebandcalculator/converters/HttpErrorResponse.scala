/*
 * Copyright 2022 HM Revenue & Customs
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

import play.api.i18n.Messages
import play.api.libs.json._

object HttpErrorResponse {

  def apply(status: Int, messageKey: String, errors: Seq[(String, String)] = Seq())(implicit messages: Messages) = {
    val responseErrors =
      if (errors.nonEmpty) { Seq("errors" -> JsObject(errors.map { case (key, value) => (key, JsString(messages(value))) })) }
      else { Nil }
    val body = Seq("statusCode" -> JsNumber(status), "message" -> JsString(messages(messageKey))) ++ responseErrors
    JsObject(body.toMap[String, JsValue])
  }
}
