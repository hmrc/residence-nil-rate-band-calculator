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

package steps

import cucumber.api.scala.{EN, ScalaDsl}
import org.scalatest.Matchers
import play.api.inject.guice.{GuiceApplicationBuilder, GuiceableModule}
import play.api.test.TestServer

trait Env extends ScalaDsl with EN with Matchers {

  val port = 9000
  var host = s"http://localhost:$port"
  var baseUrl = s"$host/residence-nil-rate-band-calculator/"
  var server: TestServer = null

  def bindModules: Seq[GuiceableModule] = Seq()

  def shutdown() = {
    server.stop()
  }

  def startServer() = {
    server = new TestServer(port, new GuiceApplicationBuilder().bindings(bindModules:_*).build())
    server.start()
  }

  Before { scenario =>
    startServer()
  }

  After { scenario =>
    shutdown()
  }
}

object Env extends Env
