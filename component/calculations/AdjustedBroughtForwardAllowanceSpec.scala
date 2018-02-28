package calculations

import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.Application
import play.api.libs.json.Json
import uk.gov.hmrc.play.test.UnitSpec
import play.api.http.Status._
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.ws.{WSClient, WSResponse}
import helpers.jsonHelper

class AdjustedBroughtForwardAllowanceSpec extends UnitSpec with GuiceOneServerPerSuite{

  override implicit lazy val app: Application = new GuiceApplicationBuilder()
    .build()

  val calculateUrl = s"http://localhost:7112/residence-nil-rate-band-calculator/calculate"

  lazy val ws: WSClient = app.injector.instanceOf[WSClient]

  def request: WSResponse = ws.url(calculateUrl)
    .post(
      //helper factory method
    )

  val responseJson = Json.parse(
    """
      |{
      | "result":[
      |  {
      |   "totalTax":23027,
      |   "npv":927716,
      |   "taxCalcs":[
      |    {
      |     "taxType":"rent",
      |     "calcType":"slice",
      |     "taxDue":8027,
      |     "detailHeading":"This is a breakdown of how the amount of SDLT on the rent was calculated",
      |     "bandHeading":"Rent bands (£)",
      |     "detailFooter":"SDLT due on the rent",
      |     "slices":[
      |      {
      |       "from":0,
      |       "to":125000,
      |       "rate":0,
      |       "taxDue":0
      |      },{
      |       "from":125000,
      |       "to":-1,
      |       "rate":1,
      |       "taxDue":8027
      |      }
      |     ]
      |    },
      |   {
      |    "taxType":"premium",
      |    "calcType":"slab",
      |    "taxDue":15000,
      |    "rate":3
      |    }
      |   ]
      |  }
      | ]
      |}
    """.stripMargin)

  request.status shouldBe OK
  request.json shouldBe responseJson

}
