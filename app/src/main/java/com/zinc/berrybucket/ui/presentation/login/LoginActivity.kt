package com.zinc.berrybucket.ui.presentation.login

import BuildConfig
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Scaffold
import androidx.compose.ui.res.stringResource
import com.zinc.berrybucket.R
import com.zinc.berrybucket.model.DialogButtonInfo
import com.zinc.berrybucket.ui.design.theme.Gray10
import com.zinc.berrybucket.ui.design.theme.Main1
import com.zinc.berrybucket.ui.presentation.component.dialog.ApiFailDialog
import com.zinc.berrybucket.ui.presentation.component.dialog.CommonDialogView
import dagger.hilt.android.AndroidEntryPoint
import com.zinc.berrybucket.ui_common.R as CommonR

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.updateAppVersion(BuildConfig.VERSION_NAME)

        // 1. 저장된 이메일이 있는지 확인
        viewModel.checkHasLoginEmail()
        setUpObservers()

    }

    private fun setUpObservers() {
        viewModel.needToStartJoin.observe(this) {
            goToJoinScreen()

        }

//        viewModel.needToUseOtherEmail.observe(this) {
//            showNeedToNewEmail {
//                goToJoinScreen()
//            }
//        }
//
//        viewModel.emailIsAlreadyUsed.observe(this) {
//            // 이미 있는 이메일인 경우. 다른 이메일을 호출하도록 할 것.
//            Toast.makeText(this, "이미 있음", Toast.LENGTH_SHORT).show()
//        }
//
//        viewModel.goToCreateUser.observe(this) {
//            // 이메일 중복 x 확인 완료. 유저 정보 입력 화면으로 이동
//            // 닉네임 중복 체크
//
//        }

        viewModel.loginFail.observe(this) {
            showLoginFailDialog()
        }
    }

    private fun showNeedToNewEmail(goToJoin: () -> Unit) {
        setContent {
            Scaffold { _ ->
                CommonDialogView(
                    title = stringResource(id = R.string.notLoginedEamil),
                    message = stringResource(id = R.string.goToJoinMessage),
                    dismissAvailable = false,
                    negative = DialogButtonInfo(text = CommonR.string.cancel, color = Gray10),
                    positive = DialogButtonInfo(text = R.string.goToJoin, color = Main1),
                    negativeEvent = {
                        finish()
                    },
                    positiveEvent = {
                        goToJoin()
                    }
                )
            }

        }
    }

    private fun showLoginFailDialog() {
        setContent {
            Scaffold { _ ->
                ApiFailDialog(
                    title = stringResource(id = R.string.loginFail),
                    message = stringResource(id = R.string.loginRetry)
                ) {
                    finish()
                }
            }
        }
    }

    private fun goToJoinScreen() {
        setContent {
            Scaffold { _ ->

            }
        }
    }
}