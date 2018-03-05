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
