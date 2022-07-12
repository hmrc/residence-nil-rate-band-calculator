import sbt._
import play.sbt.PlayImport._
import play.core.PlayVersion


private object AppDependencies {

  val compile = Seq(
    ws,
    "uk.gov.hmrc"         %% "bootstrap-backend-play-28"   % "5.25.0",
    "org.scalaj"          %% "scalaj-http"                 % "2.4.2",
    "com.typesafe.play"   %% "play-json-joda"              % "2.9.2",
    "org.scalatestplus"   %%  "scalatestplus-mockito"     % "1.0.0-M2"
  )

  trait TestDependencies {
    lazy val scope: String = "test"
    lazy val test : Seq[ModuleID] = ???
  }

  object Test {
    def apply() = new TestDependencies {
      override lazy val test = Seq(
        "uk.gov.hmrc"               %% "bootstrap-test-play-28" % "5.24.0"        % scope,
        "org.pegdown"               %  "pegdown"            % "1.6.0"             % scope,
        "com.typesafe.play"         %% "play-test"          % PlayVersion.current % scope,
        "org.scala-tools.testing"   %  "test-interface"     % "0.5",
        "org.mockito"               %  "mockito-core"       % "3.12.4"             % scope,
        "org.scalatestplus"         %%  "scalatestplus-mockito"     % "1.0.0-M2"          % scope,
      )
    }.test
  }

  object IntegrationTest {
    def apply() = new TestDependencies {

      override lazy val scope: String = "it"

      override lazy val test = Seq(
        "org.pegdown"               % "pegdown"               % "1.6.0"                 % scope,
        "com.typesafe.play"         %% "play-test"            % PlayVersion.current     % scope,
        "uk.gov.hmrc"               %% "bootstrap-test-play-28" % "5.24.0"            % scope,
        "org.mockito"               % "mockito-core"          % "3.12.4"                 % scope
      )
    }.test
  }

  def apply() = compile ++ Test() ++ IntegrationTest()
}
