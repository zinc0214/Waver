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
        kotlinCompilerExtensionVersion = libs.versions.composeCompilerVersion.get()
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

    // androidX
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.android.material)

    // compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.bom.ui)
    implementation(libs.compose.bom.material)
    implementation(libs.compose.bom.tooling)
    implementation(libs.compose.bom.livedata)
    implementation(libs.compose.bom.runtime)

    implementation(libs.compose.accompanist.permissions)
    implementation(libs.compose.accompanist.pager)
    implementation(libs.compose.navigation)
    implementation(libs.compose.constraintlayout)
    implementation(libs.compose.accompanist.systemuicontroller)
    implementation(libs.compose.reorderable)
    implementation(libs.compose.toolbar)

    // Hilt
    implementation(libs.hilt.anroid)
    implementation(libs.hilt.navigation)
    kapt(libs.hilt.compiler)

    // Coil
    implementation(libs.coil)
}