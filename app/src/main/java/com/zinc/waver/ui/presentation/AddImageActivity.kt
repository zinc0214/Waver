package com.zinc.waver.ui.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.canhub.cropper.CropImage.ActivityResult
import com.zinc.waver.databinding.ExtendedActivityBinding

internal class AppImageActivity : AppCompatActivity() {

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
        super.onCreate(savedInstanceState)

        binding = ExtendedActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.e("ayhan", "uri : $uri")

        binding.saveBtn.setOnClickListener { cropImage() }
        binding.backBtn.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.rotateText.setOnClickListener { onRotateClick() }

        uri = intent.getParcelableExtra(EXTRA_IMAGE_URI)
        if (uri != null) {
            binding.cropImageView.setImageUriAsync(uri)
        }
    }

    private fun cropImage() {
        val result = ActivityResult(
            originalUri = binding.cropImageView.imageUri,
            uriContent = binding.cropImageView.imageUri,
            error = null,
            cropPoints = binding.cropImageView.cropPoints,
            cropRect = binding.cropImageView.cropRect,
            rotation = binding.cropImageView.rotatedDegrees,
            wholeImageRect = binding.cropImageView.wholeImageRect,
            sampleSize = 1,
        )

        val intent = Intent().apply {
            putExtra("EXTRA_KEY", "Extra data")
        }
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun onRotateClick() {
        binding.cropImageView.rotateImage(90)
    }
}
