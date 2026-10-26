package com.github.maksise4ka.chats.ui

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import com.github.maksise4ka.chats.R
import com.github.maksise4ka.chats.ui.chats.ChatsScreen
import com.github.maksise4ka.chats.ui.image.ImageScreen
import com.github.maksise4ka.chats.ui.messages.MessagesScreen

@Composable
fun ChatsApp() {
    var selectedChannel by rememberSaveable { mutableStateOf<String?>(null) }
    var openedImage by rememberSaveable { mutableStateOf<String?>(null) }
    val isLandscape = LocalConfiguration.current.orientation == Configuration.ORIENTATION_LANDSCAPE

    BackHandler(enabled = openedImage != null || selectedChannel != null) {
        if (openedImage != null) {
            openedImage = null
        } else {
            selectedChannel = null
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (isLandscape) {
                LandscapeLayout(
                    selectedChannel = selectedChannel,
                    onChannelClick = { selectedChannel = it },
                    onCloseChat = { selectedChannel = null },
                    onImageClick = { openedImage = it },
                )
            } else {
                PortraitLayout(
                    selectedChannel = selectedChannel,
                    onChannelClick = { selectedChannel = it },
                    onCloseChat = { selectedChannel = null },
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

@Composable
private fun PortraitLayout(
    selectedChannel: String?,
    onChannelClick: (String) -> Unit,
    onCloseChat: () -> Unit,
    onImageClick: (String) -> Unit,
) {
    if (selectedChannel == null) {
        ChatsScreen(
            selectedChannel = null,
            onChannelClick = onChannelClick,
        )
    } else {
        MessagesScreen(
            channel = selectedChannel,
            onBack = onCloseChat,
            onImageClick = onImageClick,
        )
    }
}

@Composable
private fun LandscapeLayout(
    selectedChannel: String?,
    onChannelClick: (String) -> Unit,
    onCloseChat: () -> Unit,
    onImageClick: (String) -> Unit,
) {
    Row {
        ChatsScreen(
            selectedChannel = selectedChannel,
            onChannelClick = onChannelClick,
            modifier = Modifier.weight(1f),
        )
        ChatPane(
            selectedChannel = selectedChannel,
            onCloseChat = onCloseChat,
            onImageClick = onImageClick,
            modifier = Modifier.weight(2f),
        )
    }
}

@Composable
private fun ChatPane(
    selectedChannel: String?,
    onCloseChat: () -> Unit,
    onImageClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (selectedChannel == null) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            Text(text = stringResource(R.string.select_chat))
        }
    } else {
        MessagesScreen(
            channel = selectedChannel,
            onBack = onCloseChat,
            onImageClick = onImageClick,
            modifier = modifier,
        )
    }
}
