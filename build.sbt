import scoverage.ScoverageKeys
import uk.gov.hmrc.DefaultBuildSettings.{defaultSettings, integrationTestSettings, scalaSettings}
import uk.gov.hmrc.sbtdistributables.SbtDistributablesPlugin.publishingSettings


lazy val appName = "residence-nil-rate-band-calculator"
lazy val appDependencies: Seq[ModuleID] = AppDependencies()
lazy val plugins : Seq[Plugins] = Seq.empty
lazy val playSettings : Seq[Setting[_]] = Seq.empty

lazy val microservice = Project(appName, file("."))
  .enablePlugins(Seq(play.sbt.PlayScala, SbtDistributablesPlugin) ++ plugins : _*)
  .disablePlugins(JUnitXmlReportPlugin) // this is an experimental plugin that is (currently) enabled by default and prevents deployment to QA environment
  .settings(testFrameworks += new TestFramework("com.waioeka.sbt.runner"))
  .settings(playSettings : _*)
  .settings(
    ScoverageKeys.coverageExcludedFiles := ".*com.kenshoo.play.metrics.*;.*Routes.*;.*uk.gov.hmrc.residencenilratebandcalculator.components.*;" +
      ".*uk.gov.hmrc.residencenilratebandcalculator.connectors.*;.*uk.gov.hmrc.residencenilratebandcalculator.filters.*;" +
      ".*uk.gov.hmrc.residencenilratebandcalculator.handlers.*;.*BuildInfo.*;" +
      ".*uk.gov.hmrc.residencenilratebandcalculator.controllers.ControllerConfiguration*;" +
      ".*uk.gov.hmrc.residencenilratebandcalculator.controllers.RamlController*",
    ScoverageKeys.coverageMinimumStmtTotal  := 90,
    ScoverageKeys.coverageFailOnMinimum := true,
    ScoverageKeys.coverageHighlighting := true,
    parallelExecution := false
  )
  .settings(scalaSettings: _*)
  .settings(scalaVersion :="2.12.12")
  .settings(publishingSettings: _*)
  .settings(defaultSettings(): _*)
  .settings(
    scalacOptions ++= Seq("-feature", "-language:implicitConversions", "-language:postfixOps"),
    scalacOptions += "-P:silencer:pathFilters=routes",
    scalacOptions += "-P:silencer:lineContentFilters=^\\w",
    dependencyOverrides += "commons-codec" % "commons-codec" % "1.12",
    libraryDependencies ++= appDependencies,
    libraryDependencies ++= Seq(
      compilerPlugin("com.github.ghik" % "silencer-plugin" % "1.7.1" cross CrossVersion.full),
      "com.github.ghik" % "silencer-lib" % "1.7.1" % Provided cross CrossVersion.full
    ),
    retrieveManaged := true,
    evictionWarningOptions in update := EvictionWarningOptions.default.withWarnScalaVersionEviction(false)
  )
  .configs(IntegrationTest)
  .settings(inConfig(IntegrationTest)(Defaults.itSettings): _*)
  .settings(integrationTestSettings())
  .settings(majorVersion := 0)
