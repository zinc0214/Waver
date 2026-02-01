plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.zinc.waver.domain"

    compileSdk = Versions.compileSdk
    buildToolsVersion = Versions.buildTools

    defaultConfig {
        minSdk = Versions.minSdk
    }

    kotlin {
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(18))
        }
    }
}

dependencies {
    implementation(project(":common"))

    // Hilt
    implementation(libs.hilt.anroid)
    kapt(libs.hilt.compiler)

    // coroutine
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
}

kapt {
    correctErrorTypes = true
}