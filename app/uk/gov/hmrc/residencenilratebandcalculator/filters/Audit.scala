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

package uk.gov.hmrc.residencenilratebandcalculator.filters

import javax.inject.Inject

import akka.stream.Materializer
import play.api.mvc.EssentialFilter
import uk.gov.hmrc.play.audit.filters.AuditFilter
import uk.gov.hmrc.play.config.AppName
import uk.gov.hmrc.residencenilratebandcalculator.connectors.MicroserviceAuditConnector
import uk.gov.hmrc.residencenilratebandcalculator.controllers.ControllerConfiguration

class Audit @Inject()(controllerConfiguration: ControllerConfiguration, microserviceAuditConnector: MicroserviceAuditConnector)
                     (implicit val mat: Materializer)
  extends EssentialFilter with AuditFilter with AppName {

  override lazy val auditConnector = microserviceAuditConnector

  override def controllerNeedsAuditing(controllerName: String) = controllerConfiguration.paramsForController(controllerName).needsAuditing
}
