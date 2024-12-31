import sbt._
import play.sbt.PlayImport._
import play.core.PlayVersion


private object AppDependencies {

  val bootstrapVersion = "9.6.0"
  val mockitoCoreVersion = "5.2.0"

  val compile: Seq[ModuleID] = Seq(
    ws,
    "uk.gov.hmrc"         %% "bootstrap-backend-play-30"   % bootstrapVersion
  )

  val testDependencies: Seq[ModuleID] = Seq(
    "uk.gov.hmrc"             %% "bootstrap-test-play-30" % bootstrapVersion,
    "org.playframework"       %% "play-test"              % PlayVersion.current,
    "org.scala-tools.testing" % "test-interface"          % "0.5",
    "org.scalatestplus"       %% "mockito-4-11"           % "3.2.17.0"
  ).map(_ % Test)

  def apply(): Seq[ModuleID] = compile ++ testDependencies
}
