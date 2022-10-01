plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
}

android {
    namespace = "com.zinc.berrybucket.ui_common"

    defaultConfig {
        minSdk = Versions.minSdk
    }

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

    implementation(project(":common"))

    // compose
    implementation(Dep.AndroidX.Compose.ui)
    implementation(Dep.AndroidX.Compose.material)
    implementation(Dep.AndroidX.Compose.tooling)
    implementation(Dep.AndroidX.Compose.activity)
    implementation(Dep.AndroidX.Compose.livedata)
    implementation(Dep.AndroidX.Compose.foundation)
    implementation(Dep.AndroidX.Compose.constraintLayout)
    implementation(Dep.AndroidX.Compose.navigation)
    implementation(Dep.AndroidX.Compose.accompanist)
    implementation(Dep.AndroidX.Compose.viewPager)
    implementation(Dep.AndroidX.Compose.indicator)
    implementation(Dep.AndroidX.Compose.flowlayout)

}