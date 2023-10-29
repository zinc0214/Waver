package com.zinc.berrybucket.ui.presentation.login

import BuildConfig
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

        viewModel.updateAppVersion(BuildConfig.VERSION_NAME)
        viewModel.loginBerryBucket()

        viewModel.joinResponse.observe(this) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        // TODO : api 실패 팝업 노출
        viewModel.failJoin.observe(this) {
            startActivity(Intent(this, HomeActivity::class.java))
            finish()
        }

        viewModel.canGoToLogin.observe(this) {
            viewModel.loginBerryBucket()
        }
    }
}