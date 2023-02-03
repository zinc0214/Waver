package com.zinc.berrybucket.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

@Parcelize
data class UserSeletedImageInfo(
    private val key: Int,
    val uri: Uri,
    val file: File
) : java.io.Serializable, Parcelable {
    fun parseKey() = key.toString() + uri.toString()
    fun intKey() = key
}