plugins {
    kotlin("plugin.serialization")
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.zinc.waver.data"

    compileSdk = Versions.compileSdk
    buildToolsVersion = Versions.buildTools

    defaultConfig {
        minSdk = Versions.minSdk
    }

    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":common"))
    implementation(project(":domain"))
    implementation(project(":datastore"))

    // Hilt
    implementation(libs.hilt.anroid)
    kapt(libs.hilt.compiler)

    // retrofit2
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.serialization)
    implementation(libs.retrofit.converter.gson)

    // okhttp3
    implementation(libs.okhttp.core)
    implementation(libs.okhttp.logging.interceptor)

    // serialization
    implementation(libs.kotlinx.serialization)
}