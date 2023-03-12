package com.zinc.data.util

import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


fun Any.toMultipartFile(key: String): MultipartBody.Part {
    return MultipartBody.Part.createFormData(key, this.toString())
}

fun File.fileToMultipartFile(key: String): MultipartBody.Part {
    return MultipartBody.Part.createFormData(
        name = key,
        filename = this.name,
        body = this.asRequestBody("image/*".toMediaType())
    )
}

