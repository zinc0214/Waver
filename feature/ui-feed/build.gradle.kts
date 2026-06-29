plugins {
    id("com.android.library")
    kotlin("android")
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    id("kotlinx-serialization")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.zinc.waver.ui_feed"

    defaultConfig {
        minSdk = Versions.minSdk
    }

    compileSdk = Versions.compileSdk
    buildToolsVersion = Versions.buildTools

    buildFeatures {
        compose = true
        viewBinding = true
    }

    kotlin {
        jvmToolchain {
            languageVersion.set(JavaLanguageVersion.of(18))
        }
    }
}


dependencies {

    implementation(project(":common"))
    implementation(project(":feature:ui-common"))
    implementation(project(":domain"))
    implementation(project(":datastore"))

    // compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.bom.ui)
    implementation(libs.compose.bom.material)
    implementation(libs.compose.bom.livedata)
    implementation(libs.compose.bom.foundation)
    implementation(libs.compose.constraintlayout)
    implementation(libs.compose.bom.tooling)
    implementation(libs.compose.accompanist.systemuicontroller)

    // Hilt
    implementation(libs.hilt.anroid)
    implementation(libs.hilt.navigation)
    implementation(libs.androidx.material3.android)
    ksp(libs.hilt.compiler)

    implementation(libs.retrofit.serialization)

    implementation(libs.shimmer)
}