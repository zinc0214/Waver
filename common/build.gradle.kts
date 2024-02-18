plugins {
    kotlin("plugin.serialization")
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.zinc.berrybucket.common"

    defaultConfig {
        minSdk = Versions.minSdk
    }

    compileSdk = Versions.compileSdk
    buildToolsVersion = Versions.buildTools

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    // serialization
    implementation(Dep.Kotlin.serialization)

    testImplementation(Dep.Test.junit)
    androidTestImplementation(Dep.Test.androidJunit)
    androidTestImplementation(Dep.Test.espressoCore)

    // Gson
    implementation(Dep.Gson.google)
    implementation(Dep.Retrofit.converter)

}