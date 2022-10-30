plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
}

android {
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    java {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
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
}

dependencies {
    implementation(Dep.Kotlin.stdlibJvm)

    implementation(project(":domain"))
    implementation(project(":data"))
    implementation(project(":common"))
    implementation(project(":datastore"))
    implementation(project(":feature:ui-common"))
    implementation(project(":feature:ui-my"))

    // android X
    implementation(Dep.AndroidX.coreKtx)
    implementation(Dep.AndroidX.appcompat)
    implementation(Dep.AndroidX.UI.material)
    implementation(Dep.AndroidX.Lifecycle.runTime)
    implementation(Dep.AndroidX.Arch.common)
    implementation(Dep.AndroidX.Arch.runtime)
    implementation(Dep.AndroidX.viewPager2)

    // test
    testImplementation(Dep.Test.junit)
    androidTestImplementation(Dep.Test.androidJunit)
    androidTestImplementation(Dep.Test.espressoCore)
    androidTestImplementation(Dep.Test.compose)

    // test - mockito
    testImplementation(Dep.Test.Mockito.core)
    testImplementation(Dep.Test.Mockito.inline)
    androidTestImplementation(Dep.Test.Mockito.android)

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
}