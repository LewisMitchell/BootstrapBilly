name := "Play28ReactiveMongo"
 
version := "1.0" 
      
lazy val `play28reactivemongo` = (project in file(".")).enablePlugins(PlayScala)

resolvers += "scalaz-bintray" at "https://dl.bintray.com/scalaz/releases"
      
resolvers += "Akka Snapshot Repository" at "https://repo.akka.io/snapshots/"
      
scalaVersion := "2.13.2"

libraryDependencies ++= Seq(
  "org.reactivemongo" %% "play2-reactivemongo" % "0.20.10-play27",
  "com.adrianhurt" %% "play-bootstrap" % "1.6.1-P28-B4",
  "org.reactivemongo" %% "reactivemongo-bson-macros" % "0.20.10"
)

libraryDependencies ++= Seq( jdbc , ehcache , ws , specs2 % Test , guice )

//unmanagedResourceDirectories in Test += { baseDirectory ( _ /"target/web/public/test" )}

      