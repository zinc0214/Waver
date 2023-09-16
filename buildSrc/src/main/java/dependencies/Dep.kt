@file:Suppress("ClassName")

object Versions {
    const val compileSdk = 33
    const val buildTools = "33.0.1"

    const val minSdk = 26
    const val targetSdk = 33
    const val versionCode = 1
    const val versionName = "1.0.0"
}

object Dep {
    object GradlePlugin {
        private const val androidStudioGradlePluginVersion = "8.1.1"
        const val android = "com.android.tools.build:gradle:$androidStudioGradlePluginVersion"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
        const val kotlinSerialization =
            "org.jetbrains.kotlin:kotlin-serialization:${Kotlin.version}"
        const val hilt = "com.google.dagger:hilt-android-gradle-plugin:${Dagger.version}"
    }

    object AndroidX {
        object activity {
            const val activityVersion = "1.7.0"
            const val activity = "androidx.activity:activity:$activityVersion"
            const val ktx = "androidx.activity:activity-ktx:$activityVersion"
        }

        const val appcompat = "androidx.appcompat:appcompat:1.4.2"
        const val coreKtx = "androidx.core:core-ktx:1.8.0"

        object Lifecycle {
            private const val lifecycleVersion = "2.6.1"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion"
            const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion"
            const val runTime = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion"
        }

        object UI {
            const val material = "com.google.android.material:material:1.6.1"
        }

        object Compose {
            const val compilerVersion = "1.3.2"
            const val version = "1.4.8"

            const val navigation = "androidx.navigation:navigation-compose:2.6.0"
            const val accompanist = "com.google.accompanist:accompanist-permissions:0.29.0-alpha"
            const val flowlayout = "com.google.accompanist:accompanist-flowlayout:0.29.0-alpha"

            const val activity =
                "androidx.activity:activity-compose:${AndroidX.activity.activityVersion}"
            const val constraintLayout =
                "androidx.constraintlayout:constraintlayout-compose:1.0.0"
            const val viewPager =
                "com.google.accompanist:accompanist-pager:0.29.0-alpha"
            const val indicator = "com.google.accompanist:accompanist-pager-indicators:0.29.0-alpha"

            object Bom {
                const val runtime = "androidx.compose.runtime:runtime"
                const val ui = "androidx.compose.ui:ui"
                const val material = "androidx.compose.material:material"
                const val tooling = "androidx.compose.ui:ui-tooling"
                const val livedata = "androidx.compose.runtime:runtime-livedata"
                const val foundation = "androidx.compose.foundation:foundation"
                // const val navigation = "androidx.navigation:navigation-compose"
            }
        }

        object Arch {
            private const val version = "2.1.0"
            const val common = "androidx.arch.core:core-common:$version"
            const val runtime = "androidx.arch.core:core-runtime:$version"
        }

        object DataStore {
            private const val version = "1.0.0"
            const val core = "androidx.datastore:datastore-preferences-core:1.0.0"
            const val dataStore = "androidx.datastore:datastore-preferences:1.0.0"
        }

        const val viewPager2 = "androidx.viewpager2:viewpager2:1.0.0"

    }

    object Dagger {
        const val version = "2.43.2"
        const val dagger = "com.google.dagger:dagger:$version"
        const val compiler = "com.google.dagger:dagger-compiler:$version"

        object Hilt {
            const val android = "com.google.dagger:hilt-android:$version"
            const val compiler = "com.google.dagger:hilt-compiler:$version"
            const val navigation = "androidx.hilt:hilt-navigation-compose:1.0.0"
        }
    }


    object Kotlin {
        const val version = "1.7.20"
        const val stdlibJvm = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"

        object coroutines {
            private const val coroutinesVersion = "1.6.4"
            const val core = "org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion"
            const val android =
                "org.jetbrains.kotlinx:kotlinx-coroutines-android:$coroutinesVersion"
            const val test = "org.jetbrains.kotlinx:kotlinx-coroutines-test:$coroutinesVersion"
        }

        const val serialization = "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.0"
    }

    object OkHttp {
        private const val version = "4.9.3"
        const val core = "com.squareup.okhttp3:okhttp:$version"
        const val loggingInterceptor = "com.squareup.okhttp3:logging-interceptor:$version"
    }

    object Gson {
        const val google = "com.google.code.gson:gson:2.9.0"
        const val squareup = "com.squareup.retrofit2:converter-gson:2.3.0"
    }

    object Retrofit {
        private const val version = "2.9.0"
        const val core = "com.squareup.retrofit2:retrofit:$version"
        const val serialization =
            "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"
        const val converter = "com.squareup.retrofit2:converter-gson:$version"
    }

    const val coil = "io.coil-kt:coil-compose:2.2.2"
    const val cropper = "com.theartofdev.edmodo:android-image-cropper:2.8.0"

    object Test {
        const val junit = "junit:junit:4.13.2"
        const val androidJunit = "androidx.test.ext:junit:1.1.3"
        const val espressoCore = "androidx.test.espresso:espresso-core:3.4.0"
        const val fragment = "androidx.fragment:fragment-testing:1.3.6"
        const val compose = "androidx.compose.ui:ui-test-junit4:${AndroidX.Compose.version}"

        object Mockito {
            private const val version = "3.2.4"

            const val core = "org.mockito:mockito-core:3.3.1"
            const val inline = "org.mockito:mockito-inline:$version"
            const val android = "org.mockito:mockito-android:$version"
        }
    }
}
