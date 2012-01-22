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
      scalaVersion := "2.9.1",
      resolvers += "JSONAR repo" at "https://github.com/seanparsons/jsonar-repo/raw/master/releases/",
      resolvers += "Sonatype Release" at "http://oss.sonatype.org/content/repositories/releases",
      libraryDependencies += "org.scribe" % "scribe" % "1.3.0",
      libraryDependencies <+= scalaVersion { "org.scala-lang" % "scala-swing" % _ },
      libraryDependencies += "com.github.seanparsons.jsonar" %% "jsonar" % "0.8.4",
      libraryDependencies += "org.scalaz" %% "scalaz-core" % "6.0.3",
      libraryDependencies += "org.sonatype.jline" % "jline" % "2.5",
      libraryDependencies += "org.fusesource.jansi" % "jansi" % "1.7",
      libraryDependencies += "org.scala-tools.testing" %% "scalacheck" % "1.9" % "test",
      libraryDependencies += "org.scalatest" % "scalatest_2.9.0" % "1.6.1" % "test",
      testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-oFD"),
      organization := "com.github.seanparsons.twitterclient",
      name := "twitterclient",
      version := "0.0.1",
      initialCommands := """
        import com.github.seanparsons.twitterclient._
        import scalaz._
        import Scalaz._
        """,
      scalacOptions ++= Seq("-deprecation"),
      fork in run := true,
      outputStrategy := Some(StdoutOutput),
      connectInput in run := true,
      publishTo := Some(Resolver.file("file",  new File(System.getProperty("maven.repo", "./target/maven-repo"))))
    )
  )
}
