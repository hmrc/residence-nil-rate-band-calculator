package helpers

import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.Application
import uk.gov.hmrc.play.test.UnitSpec
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.ws.WSClient

class BaseComponentClass extends UnitSpec with GuiceOneServerPerSuite{
  override implicit lazy val app: Application = new GuiceApplicationBuilder().build()

  val calculateUrl = s"http://localhost:7112/residence-nil-rate-band-calculator/calculate"
  def nilRateBandUrl(date: String) = s"http://localhost:7112/residence-nil-rate-band-calculator/nilrateband/$date"
  val jsonHelper: jsonHelperFactory = jsonHelperFactory()
  lazy val ws: WSClient = app.injector.instanceOf[WSClient]

}
