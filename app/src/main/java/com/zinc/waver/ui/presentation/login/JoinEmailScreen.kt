package com.zinc.waver.ui.presentation.login

import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.zinc.waver.BuildConfig
import com.zinc.waver.R
import com.zinc.waver.model.DialogButtonInfo
import com.zinc.waver.ui.design.theme.Gray1
import com.zinc.waver.ui.design.theme.Gray10
import com.zinc.waver.ui.design.theme.Gray3
import com.zinc.waver.ui.design.theme.Gray7
import com.zinc.waver.ui.design.theme.Main4
import com.zinc.waver.ui.presentation.component.MyText
import com.zinc.waver.ui.presentation.component.dialog.ApiFailDialog
import com.zinc.waver.ui.presentation.component.dialog.CommonDialogView
import kotlinx.coroutines.launch
import com.zinc.waver.ui_common.R as CommonR


// 회원가입1 > 이메일 입력
@Composable
fun JoinEmailScreen(
    goToNexPage: (String) -> Unit,
    goToBack: () -> Unit,
    goToLogin: (String) -> Unit,
) {
    val viewModel: JoinEmailViewModel = hiltViewModel()

    // 이미 이메일이 있는지?
    val checkAlreadyUsedEmailAsState by viewModel.isAlreadyUsedEmail.observeAsState()
    val isAlreadyUsedEmail = remember { mutableStateOf(false) }

    // 존재하는 이메일이 없는 경우
    val goToMakeNickNameAsState by viewModel.goToMakeNickName.observeAsState()

    // 이메일이 있는 경우, 로그인 하러 가기
    val goToLoginAsState by viewModel.goToLogin.observeAsState()

    // api 실패
    val failApiAsState by viewModel.failEmailCheck.observeAsState()
    val isFailApi = remember { mutableStateOf(false) }

    val prevLoginEmail = remember { mutableStateOf("") }

    LaunchedEffect(key1 = checkAlreadyUsedEmailAsState) {
        isAlreadyUsedEmail.value = checkAlreadyUsedEmailAsState ?: false
    }

    LaunchedEffect(key1 = goToMakeNickNameAsState) {
        goToMakeNickNameAsState?.let { data ->
            goToNexPage(data)
        }
    }

    LaunchedEffect(key1 = failApiAsState) {
        isFailApi.value = failApiAsState == true
    }

    LaunchedEffect(key1 = goToLoginAsState) {
        if (goToLoginAsState == true) {
            goToLogin(prevLoginEmail.value)
        }
    }

    GoogleSignInButton(goToEmailCheck = {
        viewModel.goToLogin(it)
        prevLoginEmail.value = it
    })

    if (isAlreadyUsedEmail.value) {
        CommonDialogView(
            title = stringResource(id = R.string.alreadyUsedEmailTitle),
            message = stringResource(id = R.string.alreadyUsedEmailDesc),
            dismissAvailable = false,
            negative = DialogButtonInfo(text = CommonR.string.closeDesc, color = Gray7),
            positive = DialogButtonInfo(text = CommonR.string.goToLogin, color = Main4),
            negativeEvent = {
                isAlreadyUsedEmail.value = false
                goToBack()
            },
            positiveEvent = {
                isAlreadyUsedEmail.value = false
                viewModel.savedLoginEmail(prevLoginEmail.value)
            }
        )
    }
    if (isFailApi.value) {
        ApiFailDialog(
            title = stringResource(id = R.string.joinFailTitle),
            message = stringResource(id = R.string.loginRetry)
        ) {
            Log.e("ayhan", "api Faaaaaa")
            isFailApi.value = false
        }
    }
}

@Composable
private fun EmailView(modifier: Modifier, emailClicked: () -> Unit) {
    ConstraintLayout(modifier = modifier) {
        val (logo, guide, button) = createRefs()

        Image(
            painter = painterResource(R.drawable.img_login_text),
            contentDescription = stringResource(id = R.string.waverJoinGuide),
            modifier = Modifier
                .constrainAs(guide) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    bottom.linkTo(parent.bottom)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
        )

        Image(
            painter = painterResource(id = R.drawable.img_wave),
            contentDescription = null,
            modifier = Modifier
                .constrainAs(logo) {
                    start.linkTo(parent.start)
                    bottom.linkTo(guide.top)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                }
                .padding(bottom = 8.dp)
        )

        Box(modifier = Modifier
            .constrainAs(button) {
                start.linkTo(parent.start)
                top.linkTo(guide.bottom)
                end.linkTo(parent.end)
                width = Dimension.fillToConstraints
            }
            .padding(start = 40.dp, end = 40.dp, top = 72.dp)
            .clickable {
                emailClicked()
            }
            .background(color = Gray1, shape = RoundedCornerShape(2.dp))
            .border(width = 1.dp, color = Gray3, shape = RoundedCornerShape(2.dp))
            .padding(top = 13.dp, bottom = 14.dp, start = 24.dp, end = 106.dp)
        ) {

            Row(
                modifier = Modifier.width(280.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.google),
                    contentDescription = null,
                    modifier = Modifier.sizeIn(18.dp)
                )

                Spacer(modifier = Modifier.weight(1f))

                MyText(
                    text = stringResource(id = R.string.goToJoin),
                    fontSize = 15.sp,
                    color = Gray10,
                    fontWeight = FontWeight.Normal
                )
            }

        }
    }
}

@Composable
fun GoogleSignInButton(goToEmailCheck: (String) -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    // Remember launcher for activity result
    val googleSignInLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            if (account != null) {
                coroutineScope.launch {
                    googleLogin(account.idToken!!, goToEmailCheck)
                }
            }
        } catch (e: ApiException) {
            // Handle sign-in error
        }
    }

    // Google Sign-In Button
    EmailView(modifier = Modifier.fillMaxSize(), emailClicked = {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(BuildConfig.GOOGLE_WEB_ID)
            .requestEmail()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(context.applicationContext, gso)
        googleSignInClient.revokeAccess().addOnCompleteListener {
            googleSignInLauncher.launch(googleSignInClient.signInIntent)
        }
    })
}

private fun googleLogin(idToken: String, goToEmailCheck: (String) -> Unit) {
    val auth: FirebaseAuth = FirebaseAuth.getInstance()

    val credential = GoogleAuthProvider.getCredential(idToken, null)
    auth.signInWithCredential(credential)
        .addOnCompleteListener { task ->
            if (task.isSuccessful) {
                // Sign in success
                Log.e("ayhan", "googleLogin : ${task.result.user?.email}")
                goToEmailCheck(task.result.user?.email.orEmpty())
            } else {
                // Sign in failed
            }
        }
}

@Preview
@Composable
private fun EmailViewPreview() {
    EmailView(modifier = Modifier.fillMaxSize()) {}
}