import sbt._

object MicroServiceBuild extends Build with MicroService {

  val appName = "residence-nil-rate-band-calculator"

  override lazy val appDependencies: Seq[ModuleID] = AppDependencies()
}

private object AppDependencies {
  import play.sbt.PlayImport._
  import play.core.PlayVersion

  private val bootstrapPlayVersion = "1.3.0"
  private val domainVersion = "5.3.0"
  private val hmrcTestVersion = "3.9.0-play-26"
  private val scalaTestVersion = "3.0.8"
  private val pegdownVersion = "1.6.0"
  private val testInterfaceVersion = "0.5"
  private val scalajHttpVersion = "2.4.2"
  private val mockitoCoreVersion = "3.2.4"
  private val scalaTestPlusVersion = "3.1.3"
  private val jsonJodaVersion = "2.6.10"

  val compile = Seq(
    ws,
    "uk.gov.hmrc" %% "bootstrap-play-26" % bootstrapPlayVersion,
    "uk.gov.hmrc" %% "domain" % domainVersion,
    "org.scalaj" %% "scalaj-http" % scalajHttpVersion,
    "com.typesafe.play" %% "play-json-joda" % jsonJodaVersion
  )

  trait TestDependencies {
    lazy val scope: String = "test"
    lazy val test : Seq[ModuleID] = ???
  }

  object Test {
    def apply() = new TestDependencies {
      override lazy val test = Seq(
        "uk.gov.hmrc" %% "hmrctest" % hmrcTestVersion % scope,
        "org.scalatest" %% "scalatest" % scalaTestVersion % scope,
        "org.pegdown" % "pegdown" % pegdownVersion % scope,
        "com.typesafe.play" %% "play-test" % PlayVersion.current % scope,
        "org.scala-tools.testing" % "test-interface" % testInterfaceVersion,
        "org.mockito" % "mockito-core" % mockitoCoreVersion % scope
      )
    }.test
  }

  object IntegrationTest {
    def apply() = new TestDependencies {

      override lazy val scope: String = "it"

      override lazy val test = Seq(
        "uk.gov.hmrc" %% "hmrctest" % hmrcTestVersion % scope,
        "org.scalatest" %% "scalatest" % scalaTestVersion % scope,
        "org.pegdown" % "pegdown" % pegdownVersion % scope,
        "com.typesafe.play" %% "play-test" % PlayVersion.current % scope,
        "org.scalatestplus.play" %% "scalatestplus-play" % scalaTestPlusVersion % scope,
        "org.mockito" % "mockito-core" % mockitoCoreVersion % scope
      )
    }.test
  }

  def apply() = compile ++ Test() ++ IntegrationTest()
}
