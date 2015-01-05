name := "JsonSample"

version := "1.0"

scalaVersion := "2.11.4"

libraryDependencies += "com.typesafe.play" %% "play-json" % "2.3.4"

lazy val runInception = taskKey[Unit]("Run the Inception main method")

fullRunTask(runInception, Runtime, "com.lucidchart.jsonsample.Inception")

lazy val runPersonSample = taskKey[Unit]("Run the PersonSample main method")

fullRunTask(runPersonSample, Runtime, "com.lucidchart.jsonsample.PersonSample")

lazy val runTransforms = taskKey[Unit]("Run the Transforms main method")

fullRunTask(runTransforms, Runtime, "com.lucidchart.jsonsample.Transforms")
