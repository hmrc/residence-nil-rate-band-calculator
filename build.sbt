import scoverage.ScoverageKeys
import uk.gov.hmrc.DefaultBuildSettings.{defaultSettings, itSettings, scalaSettings}

lazy val appName                        = "residence-nil-rate-band-calculator"
lazy val appDependencies: Seq[ModuleID] = AppDependencies()
lazy val plugins: Seq[Plugins]          = Seq.empty
lazy val playSettings: Seq[Setting[?]]  = Seq.empty

ThisBuild / majorVersion := 0
ThisBuild / scalaVersion := "3.6.4"

lazy val microservice = Project(appName, file("."))
  .enablePlugins((Seq(play.sbt.PlayScala, SbtDistributablesPlugin) ++ plugins): _*)
  .disablePlugins(JUnitXmlReportPlugin) // this is an experimental plugin that is (currently) enabled by default and prevents deployment to QA environment
  .settings(testFrameworks += new TestFramework("com.waioeka.sbt.runner"))
  .settings(playSettings: _*)
  .settings(
    ScoverageKeys.coverageExcludedFiles := ".*com.kenshoo.play.metrics.*;.*Routes.*;.*uk.gov.hmrc.residencenilratebandcalculator.components.*;" +
      ".*uk.gov.hmrc.residencenilratebandcalculator.connectors.*;.*uk.gov.hmrc.residencenilratebandcalculator.filters.*;" +
      ".*uk.gov.hmrc.residencenilratebandcalculator.handlers.*;.*BuildInfo.*;" +
      ".*uk.gov.hmrc.residencenilratebandcalculator.controllers.ControllerConfiguration*;",
    ScoverageKeys.coverageMinimumStmtTotal := 90,
    ScoverageKeys.coverageFailOnMinimum    := false,
    ScoverageKeys.coverageHighlighting     := true
  )
  .settings(scalaSettings: _*)
  .settings(defaultSettings(): _*)
  .settings(
    scalacOptions ++= Seq("-feature", "-language:implicitConversions", "-language:postfixOps"),
    scalacOptions += "-Wconf:msg=Flag.*repeatedly:s",
    scalacOptions += "-Wconf:msg=.*-Wunused.*:s",
    dependencyOverrides += "commons-codec" % "commons-codec" % "1.12",
    libraryDependencies ++= appDependencies,
    retrieveManaged := true
  )
  .settings(
    scalacOptions ++= Seq(
      // Silence unused warnings on Play `routes` files
      "-Wconf:src=routes/.*:s"
    )
  )
  .settings(PlayKeys.playDefaultPort := 7112)

lazy val it = project
  .enablePlugins(PlayScala)
  .dependsOn(microservice % "test->test")
  .settings(itSettings(): _*)
  .settings(
    scalacOptions ++= Seq(
      "-Wconf:msg=Flag.*repeatedly:s"
    )
  )
