import sbt._
import Keys._
import java.io.File
import sbtassembly.Plugin._
import AssemblyKeys._

object TwitterClientBuild extends Build {
  lazy val project = Project(
    id = "root",
    base = file("."),
    settings = Defaults.defaultSettings ++ assemblySettings ++ Seq(
      scalaVersion := "2.9.2",
      resolvers += "Sonatype Release" at "http://oss.sonatype.org/content/repositories/releases",
      libraryDependencies += "org.scribe" % "scribe" % "1.3.0",
      libraryDependencies += "org.scalaz" %% "scalaz-effect" % "7.0-SNAPSHOT",
      libraryDependencies += "org.scalaz" %% "scalaz-concurrent" % "7.0-SNAPSHOT",
      libraryDependencies <+= scalaVersion { "org.scala-lang" % "scala-swing" % _ },
      libraryDependencies += "com.github.seanparsons.jsonar" %% "jsonar-core" % "0.9.4",
      libraryDependencies += "org.scala-tools.testing" % "scalacheck_2.9.1" % "1.9" % "test",
      libraryDependencies += "org.scalatest" %% "scalatest" % "1.8" % "test",
      testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oFD"),
      organization := "com.github.seanparsons.twitterclient",
      name := "twitterclient",
      version := "0.0.2",
      initialCommands := """
        import com.github.seanparsons.twitterclient._
        import scalaz._
        import Scalaz._
        """,
      scalacOptions ++= Seq("-deprecation"),
      fork in run := true,
      publishTo := Some(Resolver.file("file",  new File(System.getProperty("maven.repo", "./target/maven-repo"))))
    )
  )
}
