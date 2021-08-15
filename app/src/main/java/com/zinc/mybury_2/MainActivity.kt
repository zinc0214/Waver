package com.zinc.mybury_2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.zinc.mybury_2.compose.theme.BaseTheme
import com.zinc.mybury_2.compose.ui.component.BucketCircularProgressBar
import com.zinc.mybury_2.compose.ui.component.ProflieImageView

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
//        setContent {
//            BaseTheme {
//                // A surface container using the 'background' color from the theme
//                Surface(color = MaterialTheme.colors.background) {
//                    BucketProgress()
//                }
//            }
//        }
    }
}

@Composable
fun BucketProgress() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            BucketCircularProgressBar()
            Spacer(Modifier.width(8.dp))
            ProflieImageView(
                percentage = 0.9f
            )
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    BaseTheme {
        BucketProgress()
    }
}