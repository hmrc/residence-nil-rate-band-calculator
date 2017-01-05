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

package uk.gov.hmrc.residencenilratebandcalculator.controllers

import javax.inject.{Inject, Singleton}

import play.api.mvc.{Action, AnyContent}
import uk.gov.hmrc.play.microservice.controller.BaseController

import scala.concurrent.Future

@Singleton
class RamlController @Inject()() extends BaseController {
  def getRaml: Action[AnyContent] = Action.async {
    Future.successful(Ok.sendResource(
      resource = "Microservice.raml",
      inline = true).withHeaders(CONTENT_TYPE -> "text/plain"))
  }

  def getSchema(filename: String): Action[AnyContent] = Action.async {
    Future.successful(Ok.sendResource(
      resource = s"schemas/$filename",
      inline = true).withHeaders(CONTENT_TYPE -> "text/plain"))
  }
}
