plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.zinc.berrybucket.ui_my"

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
        viewBinding = true
    }

    kapt {
        correctErrorTypes = true
    }
}


dependencies {

    implementation(project(":common"))
    implementation(project(":ui-common"))
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":common"))
    implementation(project(":datastore"))

    // androidX
    implementation(Dep.AndroidX.coreKtx)
    implementation(Dep.AndroidX.appcompat)
    implementation(Dep.AndroidX.UI.material)

    // compose
    implementation(Dep.AndroidX.Compose.ui)
    implementation(Dep.AndroidX.Compose.material)
    implementation(Dep.AndroidX.Compose.livedata)
    implementation(Dep.AndroidX.Compose.runtime)
    implementation(Dep.AndroidX.Compose.accompanist)
    implementation(Dep.AndroidX.Compose.tooling)
    implementation(Dep.AndroidX.Compose.viewPager)
    implementation(Dep.AndroidX.Compose.navigation)
    implementation(Dep.AndroidX.Compose.constraintLayout)

    // Hilt
    implementation(Dep.Dagger.Hilt.android)
    implementation(Dep.Dagger.Hilt.navigation)
    kapt(Dep.Dagger.Hilt.compiler)
}