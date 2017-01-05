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

package utils

import steps.{Context, Env}
import scalaj.http._

object HttpConnector {

  def post(endpoint: String, body: String) = {
    val response = Http(s"${Env.baseUrl}$endpoint").postData(body).header("content-type", "application/json").asString

    Context.responseCode = response.code
    Context.responseBody = response.body
  }
}
