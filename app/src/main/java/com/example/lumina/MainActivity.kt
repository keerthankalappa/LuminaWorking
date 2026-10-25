package com.example.lumina

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.lumina.audio.LuminaSoundManager
import com.example.lumina.data.storage.LuminaDatabase
import com.example.lumina.ui.navigation.LuminaApp
import com.example.lumina.ui.theme.LuminaTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val database = LuminaDatabase.getInstance(this)
        val soundManager = LuminaSoundManager.getInstance(this)

        setContent {
            LuminaTheme {
                LuminaApp(
                    database = database,
                    soundManager = soundManager
                )
            }
        }
    }
}
