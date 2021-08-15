// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    val composeVersion by extra("1.0.0-beta09")
    val hiltVersion by extra("2.37")
    val androidXHiltVersion by extra("1.0.0")
    val coroutinesVersion by extra("1.5.0")
    val retrofitVersion by extra("2.9.0")
    val okHttpVersion by extra("4.9.1")
    val serialization by extra("0.8.0")
    val retrofitConverterVersion by extra("2.9.0")

    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.0-beta02")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.10")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.38.1")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.5.21")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}