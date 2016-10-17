name := """mads-todolist"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

javacOptions ++= Seq("-Xlint:deprecation", "-Xlint:unchecked")

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  javaJpa,
  "org.hibernate" % "hibernate-entitymanager" % "4.3.7.Final",
  "mysql" % "mysql-connector-java" % "5.1.18"
)
