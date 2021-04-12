/*
 * Copyright 2021 HM Revenue & Customs
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

import java.nio.file.Paths

import javax.inject.{Inject, Singleton}
import play.api.mvc.{Action, AnyContent, ControllerComponents}
import uk.gov.hmrc.play.bootstrap.backend.controller.BackendController

import scala.concurrent.Future

@Singleton
class RamlController @Inject()(cc: ControllerComponents) extends BackendController(cc) {

  private def getResource(resource: String): Action[AnyContent] = Action.async {
    Future.successful(Ok.sendResource(
      resource = resource,
      inline = true).withHeaders(CONTENT_TYPE -> "text/plain"))
  }

  def getRaml(version: String): Action[AnyContent] = {
    val path = Paths.get("resources", "public", "api", "conf", version, "application.raml")
    getResource(path.toString)
  }

  def getSchema(version: String, filename: String): Action[AnyContent] = {
    val path = Paths.get("resources", "public", "api", "conf", version, "schemas", filename)
    getResource(path.toString)
  }

  def getDocs(version: String, filename: String): Action[AnyContent] = {
    val path = Paths.get("resources", "public", "api", "conf", version, "docs", filename)
    getResource(path.toString)
  }
}
