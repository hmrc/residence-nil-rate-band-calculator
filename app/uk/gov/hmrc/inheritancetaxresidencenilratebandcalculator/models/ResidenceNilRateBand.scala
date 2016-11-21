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

object ResidenceNilRateBand {
  def apply(date: LocalDate): Int = {
    val bands = Map(
      new LocalDate(2018, 4, 5) -> 125000,
      new LocalDate(2019, 4, 5) -> 150000,
      new LocalDate(2020, 4, 5) -> 175000)

    val bandDate = getHighestDateBefore(date, bands)

    bandDate.fold(100000) { d => bands(d) }
  }

  private val getHighestDateBefore: (LocalDate, Map[LocalDate, Int]) => Option[LocalDate] = (date, bands) =>
    bands.keys.toSeq.filter(d => d.isBefore(date)).sortWith(_ isAfter _).headOption
}
