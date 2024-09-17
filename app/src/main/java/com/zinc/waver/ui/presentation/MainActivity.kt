package com.zinc.waver.ui.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.zinc.waver.ui.design.theme.BaseTheme
import com.zinc.waver.ui.design.theme.Gray1

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BaseTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Gray1
                ) {
                    MyScreenContent()
                }
            }
        }
    }
}

@Composable
fun MyScreenContent() {
    val systemUiController = rememberSystemUiController()

    // Set status bar and navigation bar colors
    systemUiController.setStatusBarColor(Color.Blue)
    systemUiController.setNavigationBarColor(Color.Green)

    // Optional: set status bar icons to be light or dark
    //systemUiController.isStatusBarDarkContentEnabled = true

    // Your Composable content
    Surface(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Gray),
        color = Gray1
    ) {
        // Your UI content here
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMyScreenContent() {
    BaseTheme {
        MyScreenContent()
    }
}
