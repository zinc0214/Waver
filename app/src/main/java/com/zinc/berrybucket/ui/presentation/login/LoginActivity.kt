package com.zinc.berrybucket.ui.presentation.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.zinc.berrybucket.ui.presentation.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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