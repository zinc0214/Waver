plugins {
    id("com.android.library")
    kotlin("android")
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
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
    ksp(libs.hilt.compiler)

    // coroutine
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
}