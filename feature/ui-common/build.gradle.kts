plugins {
    id("com.android.library")
    kotlin("android")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
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
    kapt {
        correctErrorTypes = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    implementation(project(":common"))
    implementation(project(":datastore"))

    val composeBom = platform(Dep.AndroidX.Compose.Bom.version)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // compose
    implementation(Dep.AndroidX.Compose.Bom.ui)
    implementation(Dep.AndroidX.Compose.Bom.material)
    implementation(Dep.AndroidX.Compose.Bom.tooling)
    implementation(Dep.AndroidX.Compose.Bom.livedata)
    implementation(Dep.AndroidX.Compose.Bom.foundation)
    implementation(Dep.AndroidX.Compose.activity)
    implementation(Dep.AndroidX.Compose.constraintLayout)
    implementation(Dep.AndroidX.Compose.navigation)
    implementation(Dep.AndroidX.Compose.accompanist)
    implementation(Dep.AndroidX.Compose.viewPager)
    implementation(Dep.AndroidX.Compose.indicator)
    implementation(Dep.AndroidX.Compose.flowlayout)
    implementation("org.burnoutcrew.composereorderable:reorderable:0.9.6")

    // Hilt
    implementation(Dep.Dagger.Hilt.android)
    implementation(Dep.Dagger.Hilt.navigation)
    kapt(Dep.Dagger.Hilt.compiler)

    // Coil
    implementation(Dep.coil)

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
}