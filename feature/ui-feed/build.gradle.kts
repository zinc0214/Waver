plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("kotlinx-serialization")
}

android {
    namespace = "com.zinc.berrybucket.ui_feed"

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

    val composeBom = platform(Dep.AndroidX.Compose.Bom.version)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // compose
    implementation(Dep.AndroidX.Compose.Bom.ui)
    implementation(Dep.AndroidX.Compose.Bom.material)
    implementation(Dep.AndroidX.Compose.Bom.livedata)
    implementation(Dep.AndroidX.Compose.Bom.foundation)
    implementation(Dep.AndroidX.Compose.constraintLayout)
    implementation(Dep.AndroidX.Compose.systemUiController)

    // Hilt
    implementation(Dep.Dagger.Hilt.android)
    implementation(Dep.Dagger.Hilt.navigation)
    kapt(Dep.Dagger.Hilt.compiler)

    implementation(Dep.Retrofit.serialization)
}