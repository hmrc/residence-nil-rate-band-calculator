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

package calculations

import helpers.BaseComponentClass
import org.joda.time.LocalDate

/*   Tests of the calculaton for person's former allowance, which is calculated as:

  RNRB on property change + Value Being Transferred + Excess of Value Being Transferred at death over Value Being Transferred

  Where "excess BFA" = (adjusted BFA) - BFA

  However, Value Being Transferred is reduced to 0 if the property was changed before 6 April 2017*/

class PersonsFormerAllowanceSpec extends BaseComponentClass{

  "Persons Former Allowance" should {
    "return the correctly calculated value" when {
      "Case 1" in {
        calculator.personsFormerAllowance(LocalDate.parse("2017-04-05"), 100000, 50000, 0) shouldBe 100000
      }

      "Case 2" in {
        calculator.personsFormerAllowance(LocalDate.parse("2017-04-06"), 100000, 50000, 0) shouldBe 150000
      }

      "Case 3" in {
        calculator.personsFormerAllowance(LocalDate.parse("2017-04-06"), 100000, 50000, 50000) shouldBe 150000
      }

      "Case 4" in {
        calculator.personsFormerAllowance(LocalDate.parse("2017-04-06"), 100000, 50000, 60000) shouldBe 160000
      }
    }
  }
}
