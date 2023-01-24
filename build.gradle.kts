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
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.20-dev-1059")
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}