package com.zinc.waver.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class LoadedImageInfo(
    private val key: Int,
    val uri: Uri,
    val file: File,
    val path: String
) : java.io.Serializable, Parcelable