package com.zinc.mybury_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.databinding.DataBindingUtil
import com.zinc.mybury_2.compose.ui.component.ProfileImageView
import com.zinc.mybury_2.databinding.ActivityHomeBinding

class MainActivity : ComponentActivity() {
    private lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home)
        binding.bucketDefault.progressBar.bucketCircularProgressBar.apply {
            setOnClickListener {
                binding.bucketDefault.progressBar.bucketCircularProgressBar.progress = 100
            }

        }

    }
}

@Composable
fun BucketProgress() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            //  BucketCircularProgressBar()
            Spacer(Modifier.width(8.dp))
            ProfileImageView(
                percentage = 0.9f
            )
        }
    }
}
