plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
}

android {

    namespace = "com.zinc.berrybucket"

    compileSdk = Versions.compileSdk
    buildToolsVersion = Versions.buildTools

    defaultConfig {
        applicationId = "com.zinc.berrybucket"
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
        versionCode = Versions.versionCode
        versionName = Versions.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        compose = true
        dataBinding = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Dep.AndroidX.Compose.compilerVersion
    }
    kapt {
        correctErrorTypes = true
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":common"))
    implementation(project(":datastore"))
    implementation(project(":feature:ui-common"))
    implementation(project(":feature:ui-my"))
    implementation(project(":feature:ui-feed"))
    implementation(project(":feature:ui-more"))
    implementation(project(":feature:ui-write"))
    implementation(project(":feature:ui-other"))
    implementation(project(":feature:ui-search"))

    // android X
    implementation(Dep.AndroidX.coreKtx)
    implementation(Dep.AndroidX.appcompat)
    implementation(Dep.AndroidX.UI.material)
    implementation(Dep.AndroidX.Lifecycle.runTime)
    implementation(Dep.AndroidX.Arch.common)
    implementation(Dep.AndroidX.Arch.runtime)
    implementation(Dep.AndroidX.viewPager2)
    implementation(project(":feature:ui-search"))

    // test
    testImplementation(Dep.Test.junit)
    androidTestImplementation(Dep.Test.androidJunit)
    androidTestImplementation(Dep.Test.espressoCore)

    // test - mockito
    testImplementation(Dep.Test.Mockito.core)
    testImplementation(Dep.Test.Mockito.inline)
    androidTestImplementation(Dep.Test.Mockito.android)

    // compose
    implementation(Dep.AndroidX.Compose.Bom.ui)
    implementation(Dep.AndroidX.Compose.Bom.material)
    implementation(Dep.AndroidX.Compose.Bom.tooling)
    implementation(Dep.AndroidX.Compose.activity)
    implementation(Dep.AndroidX.Compose.Bom.livedata)
    implementation(Dep.AndroidX.Compose.Bom.foundation)
    implementation(Dep.AndroidX.Compose.constraintLayout)
    implementation(Dep.AndroidX.Compose.navigation)
    implementation(Dep.AndroidX.Compose.accompanist)
    implementation(Dep.AndroidX.Compose.viewPager)
    implementation(Dep.AndroidX.Compose.indicator)
    implementation(Dep.AndroidX.Compose.flowlayout)
    implementation(Dep.AndroidX.Compose.systemUiController)

    // Hilt
    implementation(Dep.Dagger.Hilt.android)
    implementation(Dep.Dagger.Hilt.navigation)
    kapt(Dep.Dagger.Hilt.compiler)

    // retrofit2
    implementation(Dep.Retrofit.core)
    implementation(Dep.Retrofit.serialization)
    implementation(Dep.Retrofit.converter)

    // okhttp3
    implementation(Dep.OkHttp.core)
    implementation(Dep.OkHttp.loggingInterceptor)

    // LiveData
    implementation(Dep.AndroidX.Lifecycle.viewModel)
    implementation(Dep.AndroidX.Lifecycle.runTime)
    implementation(Dep.AndroidX.Lifecycle.livedata)

    // coil
    implementation(Dep.coil)

    // cropper
    implementation(Dep.cropper)

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
}