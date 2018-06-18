enablePlugins(AndroidLib)

name := baseDirectory.value.getName

organization := "hobby.wei.c.anno"

version := "1.0.0"

scalaVersion := "2.11.7"

// 等同于两句：targetSdkVersion, compileSdkVersion
platformTarget in Android := "android-26"

buildToolsVersion in Android := Some("26.0.1")

minSdkVersion in Android := "5"

// 如果要用 jitpack 打包的话就加上，打完了再注掉。
resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies ++= Seq(
// 如果要用 jitpack 打包的话就加上，打完了再注掉。
  "com.github.dedge-space" % "annoguard" % "1.0.3-beta",
  "com.github.dedge-space" % "scala-lang" % "148431ce18"
)
