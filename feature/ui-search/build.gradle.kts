plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.zinc.berrybucket.ui_search"

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

    // android X
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.arch.runtime)

    // compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.bom.ui)
    implementation(libs.compose.bom.foundation)
    implementation(libs.compose.bom.material)
    implementation(libs.compose.bom.livedata)
    implementation(libs.compose.bom.ui)

    implementation(libs.compose.accompanist.permissions)
    implementation(libs.compose.constraintlayout)
    implementation(libs.compose.accompanist.systemuicontroller)

    // Hilt
    implementation(libs.hilt.anroid)
    implementation(libs.hilt.navigation)
    kapt(libs.hilt.compiler)

}