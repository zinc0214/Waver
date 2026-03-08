package com.zinc.waver.util

import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.canhub.cropper.CropImageContractOptions
import com.canhub.cropper.CropImageView

/**
 * 커스텀 이미지 크롭 ActivityResultContract
 * CropImageContractOptions를 Input으로 받아 처리
 */
@Suppress("DEPRECATION")
class CropImageCustomContract :
    ActivityResultContract<CropImageContractOptions, CropImageView.CropResult>() {

    private val delegate = com.canhub.cropper.CropImageContract()

    override fun createIntent(context: Context, input: CropImageContractOptions): Intent {
        return delegate.createIntent(context, input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): CropImageView.CropResult {
        return delegate.parseResult(resultCode, intent)
    }
}
