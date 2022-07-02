@file:Suppress("ClassName")

object Versions {
    const val compileSdk = 31
    const val buildTools = "31.0.0"

    const val minSdk = 24
    const val targetSdk = 31
    const val versionCode = 1
    const val versionName = "1.0.0"
}

object Dep {
    object GradlePlugin {
        private const val androidStudioGradlePluginVersion = "7.1.3"
        const val android = "com.android.tools.build:gradle:$androidStudioGradlePluginVersion"
        const val kotlin = "org.jetbrains.kotlin:kotlin-gradle-plugin:${Kotlin.version}"
        const val kotlinSerialization =
            "org.jetbrains.kotlin:kotlin-serialization:${Kotlin.version}"
        const val hilt = "com.google.dagger:hilt-android-gradle-plugin:${Dagger.version}"
    }

    object AndroidX {
        object activity {
            const val activityVersion = "1.4.0"
            const val activity = "androidx.activity:activity:$activityVersion"
            const val ktx = "androidx.activity:activity-ktx:$activityVersion"
        }

        const val appcompat = "androidx.appcompat:appcompat:1.4.1"
        const val coreKtx = "androidx.core:core-ktx:1.7.0"

        object fragment {
            private const val fragmentVersion = "1.4.1"
            const val fragment = "androidx.fragment:fragment:$fragmentVersion"
            const val ktx = "androidx.fragment:fragment-ktx:$fragmentVersion"
        }

        object Lifecycle {
            private const val lifecycleVersion = "2.4.1"
            const val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion"
            const val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion"
            const val runTime = "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion"
        }

        object UI {
            const val material = "com.google.android.material:material:1.5.0"
        }

        object Compose {
            const val version = "1.2.0-beta02"

            const val runtime = "androidx.compose.runtime:runtime:$version"
            const val ui = "androidx.compose.ui:ui:${version}"
            const val material = "androidx.compose.material:material:${version}"
            const val tooling = "androidx.compose.ui:ui-tooling:${version}"
            const val livedata = "androidx.compose.runtime:runtime-livedata:$version"
            const val foundation = "androidx.compose.foundation:foundation:$version"
            const val navigation = "androidx.navigation:navigation-compose:2.4.2"

            const val activity =
                "androidx.activity:activity-compose:${AndroidX.activity.activityVersion}"
            const val constraintLayout =
                "androidx.constraintlayout:constraintlayout-compose:1.0.0"
            const val viewPager =
                "com.google.accompanist:accompanist-pager:0.23.1"
            const val indicator = "com.google.accompanist:accompanist-pager-indicators:0.23.1"
        }

        object RecyclerView {
            const val core = "androidx.recyclerview:recyclerview:1.2.1"
            const val selection = "androidx.recyclerview:recyclerview-selection:1.1.0"
        }

        object Arch {
            private const val version = "2.1.0"
            const val common = "androidx.arch.core:core-common:$version"
            const val runtime = "androidx.arch.core:core-runtime:$version"
        }

        const val viewPager2 = "androidx.viewpager2:viewpager2:1.0.0"
    }

    object Dagger {
        const val version = "2.41"
        const val dagger = "com.google.dagger:dagger:$version"
        const val compiler = "com.google.dagger:dagger-compiler:$version"

        object Hilt {
            const val android = "com.google.dagger:hilt-android:$version"
            const val compiler = "com.google.dagger:hilt-compiler:$version"
            const val navigation = "androidx.hilt:hilt-navigation-compose:1.0.0"
        }
    }


    object Kotlin {
        const val version = "1.6.21"
        const val stdlibJvm = "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$version"

        object coroutines {
            private const val coroutinesVersion = "1.6.0"
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

    object Retrofit {
        private const val version = "2.9.0"
        const val core = "com.squareup.retrofit2:retrofit:$version"
        const val serialization =
            "com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0"
        const val converter = "com.squareup.retrofit2:converter-gson:$version"
    }

    const val glide = "com.github.bumptech.glide:4.13.1"
    const val cardView = "androidx.cardview:cardview:1.0.0"
    const val flexBox = "com.google.android:flexbox:2.0.1"


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
