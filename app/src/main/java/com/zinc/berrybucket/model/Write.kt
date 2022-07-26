package com.zinc.berrybucket.model

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.File

data class WriteOption(
    val type: WriteOptionsType1,
    val title: String,
    val content: String
) : java.io.Serializable

@Parcelize
data class WriteImageInfo(
    private val key: Int,
    val uri: Uri,
    val file: File
) : java.io.Serializable, Parcelable {
    fun parseKey() = key.toString() + uri.toString()
    fun intKey() = key
}

data class WriteAddOption(
    val type: WriteOptionsType2,
    val title: String,
    val tagList: List<String>,
    val clicked: (WriteOptionsType2) -> Unit
) : java.io.Serializable

@Parcelize
data class WriteInfo1(
    val title: String = "",
    val memo: String = "",
    val images: List<WriteImageInfo> = emptyList(),
    val options: List<WriteOption> = emptyList(),
) : java.io.Serializable, Parcelable

data class WriteResultInfo(
    val writeInfo1: WriteInfo1,
    val keyWord: List<String>,
    val tagFriends: List<String>,
    val isScrapAvailable: Boolean = false
) : java.io.Serializable

data class WriteKeyWord(
    val id: String,
    val text: String
)

enum class WriteOptionsType1 {
    MEMO, IMAGE, CATEGORY, D_DAY, GOAL
}

interface WriteOptionsType2 {
    object TAG : WriteOptionsType2
    object FRIENDS : WriteOptionsType2
}