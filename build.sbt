enablePlugins(AndroidLib)

name := baseDirectory.value.getName

organization := "hobby.wei.c.anno"

version := "1.0.0"

scalaVersion := "2.11.7"

// 等同于两句：targetSdkVersion, compileSdkVersion
platformTarget in Android := "android-26"

buildToolsVersion in Android := Some("26.0.1")

minSdkVersion in Android := "18"
