package com.zinc.waver.ui.presentation

import android.app.Activity
import android.content.Intent
import android.content.Intent.createChooser
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.zinc.waver.R
import com.zinc.waver.model.AddImageType
import com.zinc.waver.ui.presentation.login.JoinScreen
import com.zinc.waver.ui.presentation.login.LoginScreen
import com.zinc.waver.ui.presentation.model.ActionWithActivity
import com.zinc.waver.ui.presentation.screen.billing.ChooseSubscription
import com.zinc.waver.ui.util.CheckPermissionView
import com.zinc.waver.ui_detail.model.ShowParentScreenType
import com.zinc.waver.util.createImageFile
import com.zinc.waver.util.getImageFileWithImageInfo
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

    private val cameraLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                photoUri?.let {
                    cropImage(it)
                    MediaScannerConnection.scanFile(
                        this, arrayOf(it.path), null
                    ) { _, _ -> }
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
                    WaverApp(action = {
                        when (it) {
                            is ActionWithActivity.AddImage -> {
                                // AddImageActivity.startWithLauncher(this, it.type)
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
                                val subs = ChooseSubscription(this) {
                                    Toast.makeText(this, "구매완료", Toast.LENGTH_SHORT).show()
                                }
                                subs.billingSetup(waverPlusType = it.type)
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
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                if (resultCode == Activity.RESULT_OK) {
                    result.uri?.let {
                        photoUri = result.uri
                        getFile()
                    }
                } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                    val error = result.error
                    Toast.makeText(
                        this,
                        resources.getString(R.string.failToGetImage),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
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
        CropImage.activity(photoUri).setGuidelines(CropImageView.Guidelines.ON)
            .setAllowFlipping(false).setAspectRatio(1, 1)
            .setScaleType(CropImageView.ScaleType.CENTER_CROP)
            .setCropShape(CropImageView.CropShape.RECTANGLE).start(this)
    }

    private fun getFile() {
        if (photoUri == null) {
            Toast.makeText(
                this,
                resources.getString(R.string.failToGetImage),
                Toast.LENGTH_SHORT
            ).show()
        } else {
            val info = getImageFileWithImageInfo(photoUri!!, imageCount++)
            if (photoUri == null) {
                Toast.makeText(
                    this,
                    resources.getString(R.string.failToGetImage),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                takePhotoAction.succeed(info!!)
            }
        }
    }

    private fun goToContactToMyBuryByEmail() {
        val send = Intent(Intent.ACTION_SENDTO)
        val uriText = "mailto:" + Uri.encode("mybury.info@gmail.com") +
                "?subject=" + Uri.encode("<" + resources.getString(R.string.goToWaveQna) + ">")
        val uri = Uri.parse(uriText)

        send.data = uri
        ContextCompat.startActivity(
            this,
            createChooser(send, resources.getString(R.string.goToWaveQna)),
            null
        )
    }
}