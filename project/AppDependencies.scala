import sbt._
import play.sbt.PlayImport._
import play.core.PlayVersion


private object AppDependencies {

  val compile = Seq(
    ws,
    "uk.gov.hmrc"         %% "bootstrap-backend-play-27"   % "3.3.0",
    "uk.gov.hmrc"         %% "domain"                      % "5.11.0-play-27",
    "org.scalaj"          %% "scalaj-http"                 % "2.4.2",
    "com.typesafe.play"   %% "play-json-joda"              % "2.8.1"
  )

  trait TestDependencies {
    lazy val scope: String = "test"
    lazy val test : Seq[ModuleID] = ???
  }

  object Test {
    def apply() = new TestDependencies {
      override lazy val test = Seq(
        "org.scalatestplus.play"    %% "scalatestplus-play" % "4.0.3"             % scope,
        "org.pegdown"               %  "pegdown"            % "1.6.0"             % scope,
        "com.typesafe.play"         %% "play-test"          % PlayVersion.current % scope,
        "org.scala-tools.testing"   %  "test-interface"     % "0.5",
        "org.mockito"               %  "mockito-core"       % "3.7.7"             % scope
      )
    }.test
  }

  object IntegrationTest {
    def apply() = new TestDependencies {

      override lazy val scope: String = "it"

      override lazy val test = Seq(
        "org.pegdown"               % "pegdown"               % "1.6.0"                 % scope,
        "com.typesafe.play"         %% "play-test"            % PlayVersion.current     % scope,
        "org.scalatestplus.play"    %% "scalatestplus-play"   % "3.1.3"                 % scope,
        "org.mockito"               % "mockito-core"          % "2.3.4"                 % scope
      )
    }.test
  }

  def apply() = compile ++ Test() ++ IntegrationTest()
}
