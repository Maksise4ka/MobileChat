package com.github.maksise4ka.chats

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import com.github.maksise4ka.chats.ui.chats.ChatsScreen
import com.github.maksise4ka.chats.ui.image.ImageScreen
import com.github.maksise4ka.chats.ui.messages.MessagesScreen
import com.github.maksise4ka.chats.ui.theme.ChatsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatsTheme {
                var selectedChannel by rememberSaveable { mutableStateOf<String?>(null) }
                var openedImage by rememberSaveable { mutableStateOf<String?>(null) }
                val isLandscape =
                    LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

                BackHandler(enabled = openedImage != null || selectedChannel != null) {
                    if (openedImage != null) {
                        openedImage = null
                    } else {
                        selectedChannel = null
                    }
                }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {
                        val channel = selectedChannel
                        if (isLandscape) {
                            Row {
                                ChatsScreen(
                                    selectedChannel = channel,
                                    onChannelClick = { selectedChannel = it },
                                    modifier = Modifier.weight(1f),
                                )
                                Box(modifier = Modifier.weight(2f)) {
                                    if (channel == null) {
                                        Text(
                                            text = stringResource(R.string.select_chat),
                                            modifier = Modifier.align(Alignment.Center),
                                        )
                                    } else {
                                        MessagesScreen(
                                            channel = channel,
                                            onBack = { selectedChannel = null },
                                            onImageClick = { openedImage = it },
                                        )
                                    }
                                }
                            }
                        } else if (channel == null) {
                            ChatsScreen(
                                selectedChannel = null,
                                onChannelClick = { selectedChannel = it },
                            )
                        } else {
                            MessagesScreen(
                                channel = channel,
                                onBack = { selectedChannel = null },
                                onImageClick = { openedImage = it },
                            )
                        }

                        val image = openedImage
                        if (image != null) {
                            ImageScreen(
                                link = image,
                                onClose = { openedImage = null },
                            )
                        }
                    }
                }
            }
        }
    }
}
