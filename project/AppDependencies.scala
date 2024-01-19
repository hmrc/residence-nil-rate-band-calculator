import sbt._
import play.sbt.PlayImport._
import play.core.PlayVersion


private object AppDependencies {

  val bootstrapVersion = "8.4.0"
  val mockitoCoreVersion = "5.2.0"

  val compile: Seq[ModuleID] = Seq(
    ws,
    "uk.gov.hmrc"         %% "bootstrap-backend-play-30"   % bootstrapVersion,
    "org.scalaj"          %% "scalaj-http"                 % "2.4.2"
  )

  val testDependencies: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-30" % bootstrapVersion,
    "org.playframework"       %% "play-test"              % PlayVersion.current,
    "org.scala-tools.testing" % "test-interface"          % "0.5",
    "org.mockito"             % "mockito-core"            % mockitoCoreVersion,
    "org.scalatestplus"       %% "scalatestplus-mockito"  % "1.0.0-M2",
  ).map(_ % Test)

  val itDependencies: Seq[ModuleID] = Seq(
    "org.playframework" %% "play-test"              % PlayVersion.current,
    "uk.gov.hmrc"       %% "bootstrap-test-play-30" % bootstrapVersion,
    "org.mockito"       % "mockito-core"            % mockitoCoreVersion
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ testDependencies ++ itDependencies
}
