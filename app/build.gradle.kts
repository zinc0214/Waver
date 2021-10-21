plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdk = Versions.compileSdk
    buildToolsVersion = Versions.buildTools

    defaultConfig {
        applicationId = "com.zinc.mybury_2"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        dataBinding = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = Dep.AndroidX.Compose.version
    }
    kapt {
        correctErrorTypes = true
    }
    testOptions {
        animationsDisabled = true
    }
    packagingOptions {
        resources.excludes.add("META-INF/LICENSE.txt")
        resources.excludes.add("META-INF/NOTICE.txt")
        resources.excludes.add("**/attach_hotspot_windows.dll")
        resources.excludes.add("META-INF/licenses/**")
        resources.excludes.add("META-INF/AL2.0")
        resources.excludes.add("META-INF/LGPL2.1")
    }
}

dependencies {
    implementation(Dep.Kotlin.stdlibJvm)

    implementation(project(":domain"))
    implementation(project(":data"))

    // android X
    implementation(Dep.AndroidX.coreKtx)
    implementation(Dep.AndroidX.appcompat)
    implementation(Dep.AndroidX.UI.material)
    implementation(Dep.AndroidX.Lifecycle.runTime)
    implementation(Dep.AndroidX.fragment.ktx)
    implementation(Dep.AndroidX.viewPager2)
    implementation("androidx.legacy:legacy-support-v4:1.0.0")

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
    implementation(Dep.AndroidX.Compose.constraintLayout)

    // Hilt
    implementation(Dep.Dagger.Hilt.android)
    kapt(Dep.Dagger.Hilt.compiler)

    // retrofit2
    implementation(Dep.Retrofit.core)
    implementation(Dep.Retrofit.serialization)
    implementation(Dep.Retrofit.converter)

    // okhttp3
    implementation(Dep.OkHttp.core)
    implementation(Dep.OkHttp.loggingInterceptor)

    // Glide
    implementation(Dep.glide)

    // CardView
    implementation(Dep.cardView)

    // RecyclerView
    implementation(Dep.AndroidX.RecyclerView.core)
    implementation(Dep.AndroidX.RecyclerView.selection)

    // FlexboxLayout
    implementation(Dep.flexBox)

}