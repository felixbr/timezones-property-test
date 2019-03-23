lazy val root = (project in file("."))
  .settings(
    inThisBuild(
      List(
        scalaVersion := "2.12.8"
      )
    ),
    name := """timezone-property-check""",
    version := "0.1.0",
    libraryDependencies ++= List(
      Dependencies.scalaTest,
      Dependencies.scalaCheck
    )
  )

scalacOptions ++= List( // useful compiler flags for scala
  "-deprecation",
  "-encoding",
  "UTF-8", // yes, this is 2 args
  "-feature",
  "-unchecked",
  "-Xfatal-warnings",
  "-Xlint:_",
  "-Ywarn-unused:-imports",
  "-Yno-adapted-args",
  "-Ywarn-numeric-widen",
  "-Xfuture",
  "-Ypartial-unification"
)

testOptions in Test := List(Tests.Argument(TestFrameworks.ScalaTest, "-oDST"))