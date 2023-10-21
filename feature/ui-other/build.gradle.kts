plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.zinc.berrybucket.ui_other"

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
    implementation(project(":data"))
    implementation(project(":datastore"))

    val composeBom = platform(Dep.AndroidX.Compose.Bom.version)
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // compose
    implementation(Dep.AndroidX.Compose.Bom.ui)

    // Hilt
    implementation(Dep.Dagger.Hilt.android)
    implementation(Dep.Dagger.Hilt.navigation)
    kapt(Dep.Dagger.Hilt.compiler)
}