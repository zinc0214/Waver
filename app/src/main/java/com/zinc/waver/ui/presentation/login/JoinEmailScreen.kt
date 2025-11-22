package com.zinc.waver.ui.presentation.login

import BuildConfig.GoogleWebClientId
import android.content.Context
import android.util.Log
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
import androidx.compose.runtime.setValue
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
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.PasswordCredential
import androidx.credentials.PublicKeyCredential
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialCustomException
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.exceptions.NoCredentialException
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GetSignInWithGoogleOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.zinc.domain.models.GoogleEmailInfo
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
import com.zinc.waver.ui_common.R as CommonR


// 회원가입1 > 이메일 입력
@Composable
fun JoinEmailScreen(
    goToNexPage: (GoogleEmailInfo) -> Unit,
    goToBack: () -> Unit,
    goToLogin: (GoogleEmailInfo) -> Unit,
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

    val prevLoginEmail = remember { mutableStateOf<GoogleEmailInfo?>(null) }


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
            prevLoginEmail.value?.let { goToLogin(it) }
        }
    }

    GoogleSignInButton2(
        goToEmailCheck = {
            viewModel.goToLogin(it)
            prevLoginEmail.value = it
        })

    if (isAlreadyUsedEmail.value) {
        CommonDialogView(
            title = stringResource(id = R.string.alreadyUsedEmailTitle),
            message = stringResource(id = R.string.alreadyUsedEmailDesc),
            dismissAvailable = false,
            leftButtonInfo = DialogButtonInfo(text = CommonR.string.closeDesc, color = Gray7),
            rightButtonInfo = DialogButtonInfo(text = CommonR.string.goToLogin, color = Main4),
            leftButtonEvent = {
                isAlreadyUsedEmail.value = false
                goToBack()
            },
            rightButtonEvent = {
                isAlreadyUsedEmail.value = false
                Log.e("ayhan", "goToLogin : ${prevLoginEmail.value}")
                prevLoginEmail.value?.let { viewModel.savedLoginEmail(it) }
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

        Box(
            modifier = Modifier
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
                    text = stringResource(id = R.string.goToJoinWithGoogle),
                    fontSize = 15.sp,
                    color = Gray10,
                    fontWeight = FontWeight.Normal
                )
            }

        }
    }
}

//This code will not work on Android versions < UPSIDE_DOWN_CAKE when GetCredentialException is
//is thrown.
suspend fun signIn(
    request: GetCredentialRequest,
    context: Context,
    goToEmailCheck: (GoogleEmailInfo) -> Unit
): Exception? {
    val credentialManager = CredentialManager.create(context)
    val failureMessage = "Sign in failed!"
    val e: Exception? = null
    //using delay() here helps prevent NoCredentialException when the BottomSheet Flow is triggered
    //on the initial running of our app
    try {
        // The getCredential is called to request a credential from Credential Manager.
        val result = credentialManager.getCredential(
            request = request,
            context = context,
        )
        Log.i("ayhan", "(☞ﾟヮﾟ)☞  Sign in Successful!  ☜(ﾟヮﾟ☜)")

        handleSignIn(result, goToEmailCheck)

    } catch (e: GetCredentialException) {
        Log.e("ayhan", "$failureMessage: Failure getting credentials", e)
        return e

    } catch (e: GoogleIdTokenParsingException) {
        Log.e("ayhan", "$failureMessage: Issue with parsing received GoogleIdToken", e)

    } catch (e: NoCredentialException) {
        Log.e("ayhan", "$failureMessage: No credentials found", e)
        return e

    } catch (e: GetCredentialCustomException) {
        Log.e("ayhan", "$failureMessage: Issue with custom credential request", e)

    } catch (e: GetCredentialCancellationException) {
        Log.e("ayhan", "$failureMessage: Sign-in was cancelled", e)
    }
    return e
}


@Composable
fun GoogleSignInButton(goToEmailCheck: (GoogleEmailInfo) -> Unit) {
    val context = LocalContext.current
    var showGoogleEmailSelect by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf("") }

    LaunchedEffect(showGoogleEmailSelect) {
        if (showGoogleEmailSelect) {
            val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(true)
                .setServerClientId(GoogleWebClientId)
                .build()

            // Create a credential request with the Google ID option.
            val request: GetCredentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            // Attempt to sign in with the created request using an authorized account
            val e = signIn(request, context, goToEmailCheck)
            // If the sign-in fails with NoCredentialException,  there are no authorized accounts.
            // In this case, we attempt to sign in again with filtering disabled.
            if (e is NoCredentialException) {
                val googleIdOptionFalse: GetGoogleIdOption = GetGoogleIdOption.Builder()
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(GoogleWebClientId)
                    .build()

                val requestFalse: GetCredentialRequest = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOptionFalse)
                    .build()

                signIn(requestFalse, context, goToEmailCheck)
            }

            showError = e?.message ?: ""

            showGoogleEmailSelect = false
        }
    }

    if (showError.isNotEmpty()) {
        CommonDialogView(
            title = stringResource(id = R.string.joinFailTitle),
            message = stringResource(id = R.string.loginRetry) + "\n${showError}",
            dismissAvailable = true,
            rightButtonInfo = DialogButtonInfo(text = CommonR.string.closeDesc, color = Gray7),
            rightButtonEvent = { showError = "" },
        )
    }

    // Google Sign-In Button
    EmailView(modifier = Modifier.fillMaxSize(), emailClicked = {
        showGoogleEmailSelect = true
    })
}

@Composable
fun GoogleSignInButton2(goToEmailCheck: (GoogleEmailInfo) -> Unit) {
    val context = LocalContext.current
    var showGoogleEmailSelect by remember { mutableStateOf(false) }
    var showError by remember { mutableStateOf("") }

    LaunchedEffect(showGoogleEmailSelect) {
        if (showGoogleEmailSelect) {
            val googleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(GoogleWebClientId)
                .build()
            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()
            try {
                val result = CredentialManager.create(context).getCredential(
                    context = context,
                    request = request
                )
                handleSignIn(result, goToEmailCheck)
            } catch (e: NoCredentialException) {
                Log.i("ayhan", "NoCredentialException: ", e)
                handleNoCredentialException(context, goToEmailCheck)
            } catch (e: GetCredentialException) {
                Log.i("ayhan", "GetCredentialException: ", e)
            }

            showGoogleEmailSelect = false
        }
    }

    if (showError.isNotEmpty()) {
        CommonDialogView(
            title = stringResource(id = R.string.joinFailTitle),
            message = stringResource(id = R.string.loginRetry) + "\n${showError}",
            dismissAvailable = true,
            rightButtonInfo = DialogButtonInfo(text = CommonR.string.closeDesc, color = Gray7),
            rightButtonEvent = { showError = "" },
        )
    }

    // Google Sign-In Button
    EmailView(modifier = Modifier.fillMaxSize(), emailClicked = {
        showGoogleEmailSelect = true
    })
}


private suspend fun handleNoCredentialException(
    context: Context,
    goToEmailCheck: (GoogleEmailInfo) -> Unit
) {
    try {
        val signInWithGoogleOption = GetSignInWithGoogleOption
            .Builder(serverClientId = GoogleWebClientId)
            .build()
        val request = GetCredentialRequest.Builder()
            .addCredentialOption(signInWithGoogleOption)
            .build()
        val result = CredentialManager.create(context).getCredential(
            context = context,
            request = request
        )
        handleSignIn(result, goToEmailCheck)
    } catch (e: Exception) {
        Log.e("ayhan", "handleNoCredentialException: ", e)
    }
}

fun handleSignIn(result: GetCredentialResponse, goToEmailCheck: (GoogleEmailInfo) -> Unit) {
    // Handle the successfully returned credential.
    val credential = result.credential
    val responseJson: String

    when (credential) {

        // Passkey credential
        is PublicKeyCredential -> {
            // Share responseJson such as a GetCredentialResponse to your server to validate and
            // authenticate
            responseJson = credential.authenticationResponseJson
            Log.e("ayhan", "PublicKeyCredential responseJson : $responseJson")
        }

        // Password credential
        is PasswordCredential -> {
            // Send ID and password to your server to validate and authenticate.
            val username = credential.id
            val password = credential.password
            Log.e("ayhan", "PasswordCredential username : $username, password : $password")
        }

        // GoogleIdToken credential
        is CustomCredential -> {
            if (credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                try {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)
                    val idToken = googleIdTokenCredential.idToken
                    val email = googleIdTokenCredential.id
                    Log.e("ayhan", "GoogleIdToken idToken : $idToken \n email : $email")
                    Log.e(
                        "ayhan",
                        "GoogleIdToken idToken : ${idToken.drop(50)}\n last : ${idToken.takeLast(100)}"
                    )
                    goToEmailCheck(GoogleEmailInfo(email = email, uid = idToken.takeLast(100)))
                } catch (e: GoogleIdTokenParsingException) {
                    Log.e("ayhan", "Received an invalid google id token response", e)
                }
            } else {
                // Catch any unrecognized custom credential type here.
                Log.e("ayhan", "Unexpected type of credential")
            }
        }

        else -> {
            // Catch any unrecognized credential type here.
            Log.e("ayhan", "Unexpected type of credential")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EmailViewPreview() {
    EmailView(modifier = Modifier.fillMaxSize()) {}
}