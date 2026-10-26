package com.github.maksise4ka.chats

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.github.maksise4ka.chats.ui.ChatsApp
import com.github.maksise4ka.chats.ui.theme.ChatsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatsTheme {
                ChatsApp()
            }
        }
    }
}
