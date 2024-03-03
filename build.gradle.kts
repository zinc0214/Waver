// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.gradle.android)
        classpath(libs.gradle.kotlin)
        classpath(libs.gradle.kotlin.serialization)
        classpath(libs.gradle.hilt)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}