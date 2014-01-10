name := "playEval"

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  jdbc,
  anorm,
  cache,
  "org.reactivemongo" %% "play2-reactivemongo" % "0.10.2",
  "org.specs2" %% "specs2" % "2.3.7" % "test"
)

scalacOptions in Test ++= Seq("-Yrangepos")

resolvers ++= Seq("snapshots", "releases").map(Resolver.sonatypeRepo)

play.Project.playScalaSettings