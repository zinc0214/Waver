package com.zinc.berrybucket.model

import android.net.Uri
import java.io.File

data class WriteOption(
    val title: String,
    val content: String
)

data class WriteImageInfo(
    val uri: Uri,
    val file: File
)

data class WriteAddOption(
    val title: String,
    val tagList: List<String>,
    val clicked: (List<String>) -> Unit
)

data class WriteInfo1(
    val title: String,
    val memo: String,
    val images: List<WriteImageInfo>,
    val options: List<WriteOption>,
) : java.io.Serializable

data class WriteResultInfo(
    val title: String,
    val memo: String,
    val images: List<WriteImageInfo>,
    val options: List<WriteOption>
) : java.io.Serializable