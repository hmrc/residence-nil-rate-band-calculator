import sbt._
import play.sbt.PlayImport._
import play.core.PlayVersion


private object AppDependencies {

  val bootstrapVersion = "7.15.0"
  val mockitoCoreVersion = "5.2.0"

  val compile = Seq(
    ws,
    "uk.gov.hmrc"         %% "bootstrap-backend-play-28"   % bootstrapVersion,
    "org.scalaj"          %% "scalaj-http"                 % "2.4.2",
    "com.typesafe.play"   %% "play-json-joda"              % "2.9.4",
  )

  trait TestDependencies {
    lazy val scope: String = "it,test"
    lazy val test : Seq[ModuleID] = ???
  }

  object Test {
    def apply() = new TestDependencies {
      override lazy val test = Seq(
        "uk.gov.hmrc"               %% "bootstrap-test-play-28" % bootstrapVersion,
        "com.typesafe.play"         %% "play-test"              % PlayVersion.current,
        "org.scala-tools.testing"   %  "test-interface"         % "0.5",
        "org.mockito"               %  "mockito-core"           % mockitoCoreVersion,
        "org.scalatestplus"         %%  "scalatestplus-mockito" % "1.0.0-M2",
      ).map(_ % scope)
    }.test
  }

  object IntegrationTest {
    def apply() = new TestDependencies {

      override lazy val scope: String = "it"

      override lazy val test = Seq(
        "com.typesafe.play"         %% "play-test"              % PlayVersion.current     % scope,
        "uk.gov.hmrc"               %% "bootstrap-test-play-28" % bootstrapVersion        % scope,
        "org.mockito"               % "mockito-core"            % mockitoCoreVersion      % scope
      )
    }.test
  }

  def apply() = compile ++ Test() ++ IntegrationTest()
}
