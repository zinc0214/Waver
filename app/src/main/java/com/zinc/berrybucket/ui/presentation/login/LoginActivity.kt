package com.zinc.berrybucket.ui.presentation.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import com.zinc.berrybucket.ui.compose.theme.BaseTheme
import com.zinc.berrybucket.ui.presentation.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // This app draws behind the system bars, so we want to handle fitting system windows
        // WindowCompat.setDecorFitsSystemWindows(window, false)

        viewModel.accessToken.observe(this) {
            if (it.isNullOrEmpty()) {
                viewModel.joinBerryBucket()
            } else {
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }

        viewModel.joinResponse.observe(this) {
            viewModel.loadToken()
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