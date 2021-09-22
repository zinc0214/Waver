// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(Dep.GradlePlugin.android)
        classpath(Dep.GradlePlugin.kotlin)
        classpath(Dep.GradlePlugin.kotlinSerialization)
        classpath(Dep.GradlePlugin.hilt)
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}