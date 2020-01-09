/*
 * Copyright 2020 HM Revenue & Customs
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

case class Percent(value: Double) extends Ordered[Percent] {

  def asDecimal: Double = value / 100

  override def compare(that: Percent) = this.value.compare(that.value)
  def -(p: Percent): Percent = Percent(this.value - p.value)
  def *(d: Double): Double = this.asDecimal * d
  def *(i: Int): Double  = this.asDecimal * i
  def *(bg: BigDecimal): Double = (this.asDecimal * bg).toDouble
}
