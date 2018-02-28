import sbt.Keys._
import sbt._
import uk.gov.hmrc.SbtAutoBuildPlugin
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin
import uk.gov.hmrc.versioning.SbtGitVersioning

object MicroServiceBuild extends Build with MicroService {

  val appName = "residence-nil-rate-band-calculator"

  override lazy val appDependencies: Seq[ModuleID] = AppDependencies()
}

private object AppDependencies {
  import play.sbt.PlayImport._
  import play.core.PlayVersion

  private val microserviceBootstrapVersion = "6.15.0"
  private val playUrlBindersVersion = "2.1.0"
  private val domainVersion = "5.1.0"
  private val hmrcTestVersion = "3.0.0"
  private val scalaTestVersion = "3.0.0"
  private val pegdownVersion = "1.6.0"
  private val cukesVersion = "1.2.4"
  private val cucumberRunnerVersion = "0.0.8"
  private val testInterfaceVersion = "0.5"
  private val scalajHttpVersion = "2.3.0"
  private val mockitoCoreVersion = "2.13.0"

  val compile = Seq(

    ws,
    "uk.gov.hmrc" %% "microservice-bootstrap" % microserviceBootstrapVersion,
    "uk.gov.hmrc" %% "play-url-binders" % playUrlBindersVersion,
    "uk.gov.hmrc" %% "domain" % domainVersion,
    "org.scalaj" %% "scalaj-http" % scalajHttpVersion
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
        "info.cukes" % "cucumber-scala_2.11" % cukesVersion,
        "info.cukes" % "cucumber-junit" % cukesVersion,
        "info.cukes" % "cucumber-core" % cukesVersion,
        "info.cukes" % "cucumber-jvm" % cukesVersion,
        "info.cukes" % "cucumber-junit" % cukesVersion,
        "org.scala-tools.testing" % "test-interface" % testInterfaceVersion,
        "com.waioeka.sbt" %% "cucumber-runner" % cucumberRunnerVersion,
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
        "com.typesafe.play" %% "play-test" % PlayVersion.current % scope
      )
    }.test
  }

  def apply() = compile ++ Test() ++ IntegrationTest()
}
