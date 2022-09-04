plugins {
    id("com.android.library")
    kotlin("android")
}

android {
    namespace = "com.zinc.berrybucket.ui_common"

    compileSdk = Versions.compileSdk
    buildToolsVersion = Versions.buildTools

    composeOptions {
        kotlinCompilerExtensionVersion = Dep.AndroidX.Compose.compilerVersion
    }

    buildFeatures {
        compose = true
    }
}

dependencies {

    // compose
    implementation(Dep.AndroidX.Compose.ui)
    implementation(Dep.AndroidX.Compose.material)
    implementation(Dep.AndroidX.Compose.tooling)
    implementation(Dep.AndroidX.Compose.foundation)
}