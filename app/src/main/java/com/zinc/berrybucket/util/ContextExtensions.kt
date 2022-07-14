package com.zinc.berrybucket.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.TypedValue
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

fun <T : Number> Context?.dp2px(dp: T, default: Int = 0) =
    if (this == null) default else TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        dp.toFloat(),
        this.resources.displayMetrics
    ).toInt()

fun Context.findActivity(): Activity? {
    var ctx = this
    while (ctx is ContextWrapper) {
        if (ctx is Activity) {
            return ctx
        }
        ctx = ctx.baseContext
    }
    return null
}

suspend fun Context.getCameraProvider(): ProcessCameraProvider = suspendCoroutine { continuation ->
    ProcessCameraProvider.getInstance(this).also { future ->
        future.addListener(
            { continuation.resume(future.get()) }, executor
        )
    }
}

val Context.executor: Executor
    get() = ContextCompat.getMainExecutor(this)