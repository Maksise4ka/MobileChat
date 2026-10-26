package com.github.maksise4ka.chats.ui.chats

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.maksise4ka.chats.R
import org.kodein.di.compose.rememberViewModel

@Composable
fun ChatsScreen(
    selectedChannel: String?,
    onChannelClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val viewModel: ChatsViewModel by rememberViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ChatsContent(
        uiState = uiState,
        selectedChannel = selectedChannel,
        onChannelClick = onChannelClick,
        onRetry = viewModel::loadChannels,
        modifier = modifier,
    )
}

@Composable
private fun ChatsContent(
    uiState: ChatsUiState,
    selectedChannel: String?,
    onChannelClick: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        ChatsUiState.Loading -> Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator()
        }

        ChatsUiState.Error -> Column(
            modifier = modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(text = stringResource(R.string.chats_load_error))
            Button(onClick = onRetry) {
                Text(text = stringResource(R.string.retry))
            }
        }

        is ChatsUiState.Content -> LazyColumn(modifier = modifier.fillMaxSize()) {
            items(uiState.channels) { channel ->
                ChannelItem(
                    channel = channel,
                    selected = channel == selectedChannel,
                    onClick = { onChannelClick(channel) },
                )
            }
        }
    }
}

@Composable
private fun ChannelItem(
    channel: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    ListItem(
        headlineContent = { Text(text = channel) },
        modifier = Modifier.selectable(selected = selected, onClick = onClick),
        colors = if (selected) {
            ListItemDefaults.colors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
        } else {
            ListItemDefaults.colors()
        },
    )
}
