package com.zinc.berrybucket.ui.presentation

import android.app.Activity
import android.content.Intent
import android.media.MediaScannerConnection
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.content.FileProvider
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import com.zinc.berrybucket.model.AddImageType
import com.zinc.berrybucket.ui.presentation.detail.model.ShowParentScreenType
import com.zinc.berrybucket.ui.presentation.login.JoinScreen
import com.zinc.berrybucket.ui.presentation.login.LoginScreen
import com.zinc.berrybucket.ui.presentation.model.ActionWithActivity
import com.zinc.berrybucket.ui.util.CheckPermissionView
import com.zinc.berrybucket.util.createImageFile
import com.zinc.berrybucket.util.getImageFileWithImageInfo
import dagger.hilt.android.AndroidEntryPoint
import java.io.File
import java.io.IOException

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

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
            val showScreenType: MutableState<ShowParentScreenType> = remember {
                mutableStateOf(ShowParentScreenType.Login)
            }

            when (showScreenType.value) {
                ShowParentScreenType.Join -> {
                    JoinScreen(goToMain = {
                        showScreenType.value = ShowParentScreenType.Main
                    }, goToBack = {
                        finish()
                    })
                }

                ShowParentScreenType.Login -> {
                    LoginScreen(goToMainHome = {
                        showScreenType.value = ShowParentScreenType.Main
                    }, goToJoin = {
                        showScreenType.value = ShowParentScreenType.Join
                    }, goToFinish = {
                        finish()
                    })
                }

                ShowParentScreenType.Main -> {
                    BerryBucketApp(action = {
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
                    Toast.makeText(this, "이미지를 가져오는데 실패했습니다1.", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show()
            e.printStackTrace()
        }

        if (photoFile != null) {
            photoUri = FileProvider.getUriForFile(
                this, "BerryBucketApplication.provider", photoFile
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
            Toast.makeText(this, "이미지를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
        } else {
            val info = getImageFileWithImageInfo(photoUri!!, imageCount++)
            if (photoUri == null) {
                Toast.makeText(this, "이미지를 가져오는데 실패했습니다.", Toast.LENGTH_SHORT).show()
            } else {
                takePhotoAction.succeed(info!!)
            }
        }
    }
}