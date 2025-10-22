plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("kotlinx-serialization")
    id("com.google.gms.google-services")
    alias(libs.plugins.compose.compiler)
}

android {

    namespace = "com.zinc.waver"

    compileSdk = Versions.compileSdk
    buildToolsVersion = Versions.buildTools

    defaultConfig {
        applicationId = "com.zinc.waver"
        minSdk = Versions.minSdk
        targetSdk = Versions.targetSdk
        versionCode = Versions.versionCode
        versionName = Versions.versionName

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        buildConfigField("String", "GOOGLE_WEB_ID", "\"${property("GOOGLE_WEB_ID")}\"")
    }

    buildFeatures {
        buildConfig = true // Enable BuildConfig
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    buildFeatures {
        compose = true
        dataBinding = true
        viewBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.composeCompilerVersion.get()
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
    implementation(project(":feature:ui-detail"))

    // androidX - common
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.android.material)

    // androidX - LiveData
    implementation(libs.androidx.lifecycle.viewmodel)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.livedata)

    // androidX - arch
    implementation(libs.androidx.arch.common)
    implementation(libs.androidx.arch.runtime)

    // androidX - etc
    implementation(libs.androidx.viewpager2)

    // test
    testImplementation(libs.test.junit)
    androidTestImplementation(libs.test.android.junit)
    androidTestImplementation(libs.test.android.espresso.core)

    // test - mockito
    testImplementation(libs.test.mockito.core)
    testImplementation(libs.test.mockito.inline)
    androidTestImplementation(libs.test.mockito.android)

    // compose
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.bom.ui)
    implementation(libs.compose.bom.material)
    implementation(libs.compose.bom.tooling)
    implementation(libs.compose.bom.livedata)
    implementation(libs.compose.bom.foundation)
    implementation(libs.compose.activity)
    implementation(libs.compose.constraintlayout)
    implementation(libs.compose.navigation)
    implementation(libs.compose.accompanist.permissions)
    implementation(libs.compose.accompanist.pager)
    implementation(libs.compose.accompanist.pager.indicators)
    implementation(libs.compose.accompanist.flowlayout)
    implementation(libs.compose.accompanist.systemuicontroller)

    // google login
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.analytics)
    implementation(libs.googleid)
    implementation(libs.play.services.auth)
    implementation(libs.androidx.credentials)

    // Hilt
    implementation(libs.hilt.anroid)
    implementation(libs.hilt.navigation)
    kapt(libs.hilt.compiler)

    // coil
    implementation(libs.coil)

    // cropper
    implementation(libs.cropper)

    // serialization
    implementation(libs.kotlinx.serialization)

    // lottie
    implementation(libs.lottie)

    // InAppBilling
    implementation(libs.inapp.billing)

    // PlayService Ads
    implementation(libs.play.services.ads)
}