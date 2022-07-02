package com.zinc.berrybucket.ui.presentation.login

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import com.zinc.berrybucket.ui.compose.theme.BaseTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This app draws behind the system bars, so we want to handle fitting system windows
        // WindowCompat.setDecorFitsSystemWindows(window, false)

        viewModel.joinBerryBucket()

        viewModel.joinResponse.observe(this) {
            Log.e("ayhan", "Response : $it")
        }

    }
}

@Composable
private fun LoginView() {
    BaseTheme {
        TextButton(onClick = {

        }) {
            Text(text = "시작하기")
        }
    }
}