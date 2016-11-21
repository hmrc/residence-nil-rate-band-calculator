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

package uk.gov.hmrc.inheritancetaxresidencenilratebandcalculator.models

import org.joda.time.LocalDate

object Calculator {

  def apply(dateOfDeath: LocalDate, estateValue: Int, propertyValue: Int, percentageBroughtForwardAllowance: Float = 0) = {

    val totalAllowance = (1 + (percentageBroughtForwardAllowance / 100)) * ResidenceNilRateBand(dateOfDeath) toInt
    val rnra = math.min(propertyValue, totalAllowance)
    val cfa = totalAllowance - rnra
    CalculationResult(rnra, cfa)
  }
}
