import helpers.BaseComponentClass
import play.api.http.Status._
import play.api.libs.ws.WSResponse

class NilRateBand extends BaseComponentClass{

  "The calculate route" should{
    "return a valid OK response" when{
      "the correct value is given for 5th April 2017" in{
        def request: WSResponse = ws.url(nilRateBandUrl("2017-04-05")).get()

        request.status shouldBe OK
        request.body shouldBe "100000"
      }

      "the correct value is given for 6th April 2017" in{
        def request: WSResponse = ws.url(nilRateBandUrl("2017-04-06")).get()

        request.status shouldBe OK
        request.body shouldBe "100000"
      }

      "the correct value is given for 1st Jan 2021" in{
        def request: WSResponse = ws.url(nilRateBandUrl("2021-01-01")).get()

        request.status shouldBe OK
        request.body shouldBe "175000"
      }
    }
  }

}
