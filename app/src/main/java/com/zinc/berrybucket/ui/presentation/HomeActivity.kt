package com.zinc.berrybucket.ui.presentation

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private val addImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val file = result.data?.getStringExtra("file")
                val url = result.data?.getStringExtra("url")

            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            BerryBucketApp(action = {
                when (it) {
                    is ActionWithActivity.AddImage -> {
                        AddImageActivity.startWithLauncher(this, it.type)
                    }
                }
            })
        }
    }
}