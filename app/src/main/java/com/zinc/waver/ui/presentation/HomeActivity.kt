package com.zinc.waver.ui.presentation

import android.content.Intent
import android.content.Intent.createChooser
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.content.FileProvider
import androidx.core.net.toUri
import com.canhub.cropper.CropImageContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageOptions
import com.canhub.cropper.CropImageView
import com.google.android.gms.ads.MobileAds
import com.zinc.waver.R
import com.zinc.waver.model.AddImageType
import com.zinc.waver.model.UserSelectedImageInfo
import com.zinc.waver.ui.presentation.login.JoinScreen
import com.zinc.waver.ui.presentation.login.LoginScreen
import com.zinc.waver.ui.presentation.model.ActionWithActivity
import com.zinc.waver.ui.presentation.model.WaverPlusType
import com.zinc.waver.ui.presentation.screen.ads.AdFullScreen
import com.zinc.waver.ui.presentation.screen.billing.ChooseSubscription
import com.zinc.waver.ui.util.CheckPermissionView
import com.zinc.waver.ui_detail.model.ShowParentScreenType
import com.zinc.waver.util.createImageFile
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val viewModel by viewModels<HomeViewModel>()

    private var photoUri: Uri? = null
    private var imageCount = 0
    private lateinit var takePhotoAction: ActionWithActivity.AddImage

    private var isNeedToShowPermission = false
    private lateinit var checkPermissionAction: ActionWithActivity.CheckPermission

    private val cropImage = registerForActivityResult(CropImageContract()) { result ->
        if (result.isSuccessful) {
            // Use the cropped image URI.
            Log.e("ayhan", "croppedImageUri : ${result.cropRect}")
            photoUri = result.uriContent
            getFile(result)
        } else {
            val exception = result.error
        }
    }

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                photoUri?.let {
                    cropImage(it)
                }
            }
        }

    private val imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            result.data?.data?.let {
                cropImage(it)
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MobileAds.initialize(this)
        checkInAppBilling()
        viewModel.loadProfileInfo()

        setContent {
            enableEdgeToEdge()

            val retryEmail: MutableState<String> = remember {
                mutableStateOf("")
            }
            val showScreenType: MutableState<ShowParentScreenType> =
                remember {
                    mutableStateOf(ShowParentScreenType.Login)
                }

            when (showScreenType.value) {
                ShowParentScreenType.Join -> {
                    JoinScreen(goToMain = {
                        showScreenType.value =
                            ShowParentScreenType.Main
                    }, goToBack = {
                        finish()
                    }, goToLogin = {
                        retryEmail.value = it
                        showScreenType.value =
                            ShowParentScreenType.Login
                    }, addImageAction = {
                        takePhotoAction = it
                        if (it.type == AddImageType.CAMERA) {
                            takePhoto()
                        } else {
                            goToGallery()
                        }
                    })
                }

                ShowParentScreenType.Login -> {
                    LoginScreen(
                        retryLoginEmail = retryEmail.value,
                        goToMainHome = {
                            showScreenType.value =
                                ShowParentScreenType.Main
                        }, goToJoin = {
                            showScreenType.value =
                                ShowParentScreenType.Join
                        }, goToFinish = {
                            finish()
                        })
                }

                ShowParentScreenType.Main -> {
                    HomeScreen(action = {
                        when (it) {
                            is ActionWithActivity.AddImage -> {
                                takePhotoAction = it
                                if (it.type == AddImageType.CAMERA) {
                                    takePhoto()
                                } else {
                                    goToGallery()
                                }
                            }

                            is ActionWithActivity.CheckPermission -> {
                                checkPermissionAction = it
                                isNeedToShowPermission = true

                            }

                            ActionWithActivity.AppFinish -> {
                                finish()
                            }

                            ActionWithActivity.GoToQNAEmail -> {
                                goToContactToMyBuryByEmail()
                            }

                            ActionWithActivity.Logout -> {
                                viewModel.logout()
                                finish()
                            }

                            is ActionWithActivity.InAppBilling -> {
                                setUpBilling(it.type, true)
                            }
                        }
                    })

                    if (isNeedToShowPermission) {
                        CheckPermissionView(isAvailable = {
                            checkPermissionAction.isAllGranted.invoke(it)
                            isNeedToShowPermission = false
                        })
                    }
                }
            }

            AdFullScreen()
        }
    }

    private fun takePhoto() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        var photoFile: File? = null

        try {
            photoFile = createImageFile(this)
        } catch (e: IOException) {
            Toast.makeText(
                this,
                resources.getString(R.string.failToGetImage),
                Toast.LENGTH_SHORT
            ).show()
            e.printStackTrace()
        }

        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(
                this, "WaverApplication.provider", photoFile
            )
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
            cameraLauncher.launch(intent)
        }
    }

    private fun goToGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        intent.putExtra("crop", true)
        intent.action = Intent.ACTION_GET_CONTENT
        imageLauncher.launch(intent)
    }

    private fun cropImage(photoUri: Uri) {
        Log.e("ayhan", "cropImage : $photoUri")
        cropImage.launch(
            CropImageContractOptions(
                uri = photoUri,
                cropImageOptions = CropImageOptions(
                    maxZoom = 3,
                    showCropLabel = true,
                    showCropOverlay = true,
                    guidelines = CropImageView.Guidelines.ON,
                    outputCompressFormat = Bitmap.CompressFormat.PNG,
                    cropMenuCropButtonTitle = getString(R.string.imageCropSave),
                    activityTitle = getString(R.string.imageCropperTitle),
                    aspectRatioX = 1,
                    aspectRatioY = 1,
                    fixAspectRatio = true,  // 이 옵션을 true로 설정하면 사용자가 비율을 변경할 수 없음
                    initialCropWindowPaddingRatio = 0f  // 크롭 윈도우가 이미지를 꽉 채우도록 설정
                )
            )
        )
    }

    private fun getFile(result: CropImageView.CropResult) {
        if (photoUri == null) {
            Toast.makeText(
                this,
                resources.getString(R.string.failToGetImage),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val imageInfo = result.uriContent?.let { uri ->
                UserSelectedImageInfo(
                    key = imageCount++,
                    uri = uri,
                    file = File(uri.path!!),
                    path = result.getUriFilePath(this).orEmpty()
                )
            }
            takePhotoAction.succeed(imageInfo!!)
        }
    }

    private fun goToContactToMyBuryByEmail() {
        val send = Intent(Intent.ACTION_SENDTO)
        val uriText = "mailto:" + Uri.encode("mybury.info@gmail.com") +
                "?subject=" + Uri.encode("<" + resources.getString(R.string.goToWaveQna) + ">")
        val uri = uriText.toUri()

        send.data = uri
        startActivity(
            createChooser(send, resources.getString(R.string.goToWaveQna)),
            null
        )
    }

    private fun checkInAppBilling() {
        WaverPlusType.entries.forEach { type ->
            setUpBilling(type, false)
        }
    }

    private fun setUpBilling(type: WaverPlusType, isForPurchase: Boolean) {
        val subs = ChooseSubscription(
            this,
            isForPurchase = isForPurchase,
            subsDone = {
                viewModel.updateWaverPlus(true)
                Toast.makeText(this, "구매완료", Toast.LENGTH_SHORT).show()
            },
            alreadyPurchased = { purchased ->
                viewModel.updateWaverPlus(purchased)
            })

        subs.billingSetup(waverPlusType = type)
    }
}