version := "0.1.0-SNAPSHOT"
scalaVersion := "2.13.10"
name := "AsyncReactiveZIOKafka"
lazy val zioVersion = "2.0.9"

libraryDependencies ++= Seq(
  "dev.zio" %% "zio"         % zioVersion,
  "dev.zio" %% "zio-streams" % zioVersion,
  "dev.zio" %% "zio-kafka"   % "2.0.7",
  "dev.zio" %% "zio-json"    % "0.4.2",
  "dev.zio" %% "zio-dynamodb" % "0.2.6",
  "dev.zio" %% "zio-test"    % zioVersion,
  "dev.zio" %% "zio-actors" % "0.1.0",
  "dev.zio" %% "zio-logging" % "2.1.10"
)

libraryDependencies ++= Seq(
  "io.circe" %% "circe-core" % "0.14.1",
  "io.circe" %% "circe-generic" % "0.14.1",
  "io.circe" %% "circe-parser" % "0.14.1"
)
