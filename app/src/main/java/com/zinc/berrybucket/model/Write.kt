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