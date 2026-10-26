package com.github.maksise4ka.chats

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.github.maksise4ka.chats.ui.chats.ChatsScreen
import com.github.maksise4ka.chats.ui.messages.MessagesScreen
import com.github.maksise4ka.chats.ui.theme.ChatsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatsTheme {
                var selectedChannel by rememberSaveable { mutableStateOf<String?>(null) }

                BackHandler(enabled = selectedChannel != null) {
                    selectedChannel = null
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val channel = selectedChannel
                    if (channel == null) {
                        ChatsScreen(
                            selectedChannel = null,
                            onChannelClick = { selectedChannel = it },
                            modifier = Modifier.padding(innerPadding),
                        )
                    } else {
                        MessagesScreen(
                            channel = channel,
                            onBack = { selectedChannel = null },
                            onImageClick = {},
                            modifier = Modifier.padding(innerPadding),
                        )
                    }
                }
            }
        }
    }
}
