plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("kotlinx-serialization")
}

android {
    namespace = "com.zinc.waver.ui_report"
    compileSdk = 34

    defaultConfig {
        minSdk = Versions.minSdk
    }

    compileSdk = Versions.compileSdk
    buildToolsVersion = Versions.buildTools

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompilerVersion.get()
    }

    buildFeatures {
        compose = true
        dataBinding = true
        viewBinding = true
    }

    kapt {
        correctErrorTypes = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(project(":common"))
    implementation(project(":feature:ui-common"))
    implementation(project(":domain"))
    implementation(project(":datastore"))

    implementation(libs.androidx.appcompat)
    implementation(libs.android.material)

    // compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.bom.ui)
    implementation(libs.compose.bom.material)
    implementation(libs.compose.bom.livedata)
    implementation(libs.compose.constraintlayout)

    implementation(libs.coil)

    // Hilt
    implementation(libs.hilt.anroid)
    implementation(libs.hilt.navigation)
    kapt(libs.hilt.compiler)

    implementation(libs.retrofit.serialization)
}