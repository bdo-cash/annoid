enablePlugins(AndroidLib)

name := baseDirectory.value.getName

organization := "hobby.wei.c.anno"

version := "1.2.1"

scalaVersion := "2.11.12"

crossScalaVersions := Seq(
  /*"2.11.7", 多余，不需要两个*/
  "2.11.12",
  /*"2.12.2", 有一些编译问题：`the interface is not a direct parent`。*/
  "2.12.12")

// 等同于两句：targetSdkVersion, compileSdkVersion
platformTarget in Android := "android-30"

buildToolsVersion in Android := Some("30.0.2")

minSdkVersion in Android := "5"

offline := true

// 如果要用 jitpack 打包的话就加上，打完了再注掉。
resolvers += "jitpack" at "https://jitpack.io"

libraryDependencies ++= Seq(
  // 如果要用 jitpack 打包的话就加上，打完了再注掉。
  "com.github.dedge-space" % "annoguard" % "v1.0.5-beta",
  "com.github.dedge-space" % "scala-lang" % "4fb85494e2"
)
