package com.zinc.berrybucket.presentation

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.zinc.berrybucket.compose.ui.BerryBucketApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This app draws behind the system bars, so we want to handle fitting system windows
        // WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {
            BerryBucketApp()
        }
    }
}