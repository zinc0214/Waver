package com.zinc.waver.ui.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.app.ActivityCompat
import com.canhub.cropper.CropImage.ActivityResult
import com.canhub.cropper.CropImageActivity
import com.zinc.waver.databinding.ExtendedActivityBinding

internal class AppImageActivity : CropImageActivity() {

    private var uri: Uri? = null

    companion object {
        private const val EXTRA_IMAGE_URI = "extra_image_uri"

        fun start(activity: Activity, uri: Uri) {
            val intent = Intent(activity, AppImageActivity::class.java).apply {
                putExtra(EXTRA_IMAGE_URI, uri)
            }
            ActivityCompat.startActivity(activity, intent, null)
        }
    }

    private lateinit var binding: ExtendedActivityBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.e("ayhan", "uri : $uri")
        binding = ExtendedActivityBinding.inflate(layoutInflater)

        super.onCreate(savedInstanceState)

        binding.saveBtn.setOnClickListener { cropImage() }
        binding.backBtn.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.rotateText.setOnClickListener { onRotateClick() }

        setCropImageView(binding.cropImageView)
    }

    override fun setContentView(view: View?) {
        super.setContentView(binding.root)
    }

    override fun onPickImageResult(resultUri: Uri?) {
        super.onPickImageResult(resultUri)

        if (resultUri != null) {
            binding.cropImageView.setImageUriAsync(resultUri)
        }
    }

    override fun getResultIntent(uri: Uri?, error: java.lang.Exception?, sampleSize: Int): Intent {
        val result = super.getResultIntent(uri, error, sampleSize)
        // Adding some more information.
        return result.putExtra("EXTRA_KEY", "Extra data")
    }

    override fun setResult(uri: Uri?, error: Exception?, sampleSize: Int) {
        val result = ActivityResult(
            originalUri = binding.cropImageView.imageUri,
            uriContent = uri,
            error = error,
            cropPoints = binding.cropImageView.cropPoints,
            cropRect = binding.cropImageView.cropRect,
            rotation = binding.cropImageView.rotatedDegrees,
            wholeImageRect = binding.cropImageView.wholeImageRect,
            sampleSize = sampleSize,
        )

        binding.cropImageView.setImageUriAsync(result.uriContent)
    }

    private fun onRotateClick() {
        binding.cropImageView.rotateImage(90)
    }
}
