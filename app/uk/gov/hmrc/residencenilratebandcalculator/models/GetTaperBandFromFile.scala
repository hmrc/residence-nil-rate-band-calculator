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

package uk.gov.hmrc.residencenilratebandcalculator.models

import javax.inject.Inject

import org.joda.time.LocalDate
import play.api.Environment

import scala.io.Source
import scala.util.{Failure, Success, Try}

class GetTaperBandFromFile @Inject()(env: Environment, filename: String) {

  private lazy val bandsAsJson: Try[String] = env.resourceAsStream(filename) match {
    case Some(stream) => Success(Source.fromInputStream(stream).mkString)
    case None => Failure(new RuntimeException("error.resource_access_failure"))
  }

  def apply(date: LocalDate): Try[TaperBand] =
    bandsAsJson.flatMap {
      json: String => GetTaperBand(date, json)
    }
}
