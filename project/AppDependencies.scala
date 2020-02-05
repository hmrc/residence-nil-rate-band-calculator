import sbt._
import play.sbt.PlayImport._
import play.core.PlayVersion


private object AppDependencies {

  val compile = Seq(
    ws,
    "uk.gov.hmrc"         %% "bootstrap-play-26"      % "1.3.0",
    "uk.gov.hmrc"         %% "domain"                 % "5.6.0-play-26",
    "org.scalaj"          %% "scalaj-http"            % "2.4.2",
    "com.typesafe.play"   %% "play-json-joda"         % "2.8.1"
  )

  trait TestDependencies {
    lazy val scope: String = "test"
    lazy val test : Seq[ModuleID] = ???
  }

  object Test {
    def apply() = new TestDependencies {
      override lazy val test = Seq(
        "uk.gov.hmrc"               %% "hmrctest"       % "3.9.0-play-26"         % scope,
        "org.scalatest"             %% "scalatest"      % "3.0.8"                 % scope,
        "org.pegdown"               % "pegdown"         % "1.6.0"                 % scope,
        "com.typesafe.play"         %% "play-test"      % PlayVersion.current     % scope,
        "org.scala-tools.testing"   % "test-interface"  % "0.5",
        "org.mockito"               % "mockito-core"    % "3.2.4"                 % scope
      )
    }.test
  }

  object IntegrationTest {
    def apply() = new TestDependencies {

      override lazy val scope: String = "it"

      override lazy val test = Seq(
        "uk.gov.hmrc"               %% "hmrctest"             % "3.9.0-play-26"         % scope,
        "org.scalatest"             %% "scalatest"            % "3.0.8"                 % scope,
        "org.pegdown"               % "pegdown"               % "1.6.0"                 % scope,
        "com.typesafe.play"         %% "play-test"            % PlayVersion.current     % scope,
        "org.scalatestplus.play"    %% "scalatestplus-play"   % "3.1.3"                 % scope,
        "org.mockito"               % "mockito-core"          % "3.2.4"                 % scope
      )
    }.test
  }

  def apply() = compile ++ Test() ++ IntegrationTest()
}
