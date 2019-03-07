import sbt.Keys._
import sbt._
import scoverage.ScoverageKeys
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin._
import uk.gov.hmrc.versioning.SbtGitVersioning.autoImport.majorVersion


trait MicroService {

  import uk.gov.hmrc._
  import DefaultBuildSettings._
  import uk.gov.hmrc.SbtAutoBuildPlugin
  import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin
  import uk.gov.hmrc.versioning.SbtGitVersioning
  import uk.gov.hmrc.SbtArtifactory

  val appName: String

  lazy val appDependencies : Seq[ModuleID] = ???
  lazy val plugins : Seq[Plugins] = Seq.empty
  lazy val playSettings : Seq[Setting[_]] = Seq.empty


  lazy val microservice = Project(appName, file("."))
    .enablePlugins(Seq(play.sbt.PlayScala,SbtAutoBuildPlugin, SbtGitVersioning, SbtDistributablesPlugin, SbtArtifactory) ++ plugins : _*)
    .settings(testFrameworks += new TestFramework("com.waioeka.sbt.runner"))
    .settings(playSettings : _*)
    .settings(
      ScoverageKeys.coverageExcludedFiles := ".*com.kenshoo.play.metrics.*;.*Routes.*;.*uk.gov.hmrc.residencenilratebandcalculator.components.*;" +
        ".*uk.gov.hmrc.residencenilratebandcalculator.connectors.*;.*uk.gov.hmrc.residencenilratebandcalculator.filters.*;" +
        ".*uk.gov.hmrc.residencenilratebandcalculator.handlers.*;.*BuildInfo.*;" +
        ".*uk.gov.hmrc.residencenilratebandcalculator.controllers.ControllerConfiguration*;" +
        ".*uk.gov.hmrc.residencenilratebandcalculator.controllers.RamlController*",
      ScoverageKeys.coverageMinimum := 90,
      ScoverageKeys.coverageFailOnMinimum := true,
      ScoverageKeys.coverageHighlighting := true,
      parallelExecution := false
    )
    .settings(scalaSettings: _*)
    .settings(scalaVersion :="2.11.12")
    .settings(publishingSettings: _*)
    .settings(defaultSettings(): _*)
    .settings(
      scalacOptions ++= Seq("-feature", "-language:implicitConversions", "-language:postfixOps"),
      libraryDependencies ++= appDependencies,
      retrieveManaged := true,
      evictionWarningOptions in update := EvictionWarningOptions.default.withWarnScalaVersionEviction(false)
    )
    .configs(IntegrationTest)
    .settings(inConfig(IntegrationTest)(Defaults.itSettings): _*)
    .settings(integrationTestSettings())
    .settings(majorVersion := 0)



}
