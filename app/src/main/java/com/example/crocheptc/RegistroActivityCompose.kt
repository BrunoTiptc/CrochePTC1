package com.example.crocheptc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily


val CustomFontFamily = FontFamily(
    Font(R.font.fonte) // se o arquivo for res/font/fonte.ttf
)

class RegistroActivityCompose : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface {
                    RegisterScreen()

                }
            }
        }
    }
}
