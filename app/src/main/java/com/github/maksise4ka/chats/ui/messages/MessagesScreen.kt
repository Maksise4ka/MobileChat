package com.github.maksise4ka.chats.ui.messages

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil3.compose.AsyncImage
import com.github.maksise4ka.chats.R
import com.github.maksise4ka.chats.data.remote.ImageUrls
import com.github.maksise4ka.chats.data.remote.model.Message
import org.kodein.di.compose.localDI
import org.kodein.di.compose.rememberInstance
import org.kodein.di.direct
import org.kodein.di.instance

private const val LOAD_MORE_THRESHOLD = 5

@Composable
fun MessagesScreen(
    channel: String,
    onBack: () -> Unit,
    onImageClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val di = localDI()
    val viewModel = viewModel(key = "messages:$channel") {
        di.direct.instance<String, MessagesViewModel>(arg = channel)
    }
    val imageUrls: ImageUrls by rememberInstance()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MessagesContent(
        channel = channel,
        uiState = uiState,
        imageUrls = imageUrls,
        onBack = onBack,
        onImageClick = onImageClick,
        onLoadMore = viewModel::loadMore,
        modifier = modifier,
    )
}

@Composable
private fun MessagesContent(
    channel: String,
    uiState: MessagesUiState,
    imageUrls: ImageUrls,
    onBack: () -> Unit,
    onImageClick: (String) -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize()) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(
                    painter = painterResource(R.drawable.ic_arrow_back),
                    contentDescription = stringResource(R.string.back_to_chats),
                )
            }
            Text(text = channel)
        }

        val listState = rememberLazyListState()
        LoadMoreEffect(listState = listState, itemsCount = uiState.messages.size, onLoadMore = onLoadMore)

        Box(modifier = Modifier.weight(1f)) {
            when {
                uiState.messages.isEmpty() && uiState.isLoading -> CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center),
                )

                uiState.messages.isEmpty() && uiState.isError -> ErrorBlock(
                    onRetry = onLoadMore,
                    modifier = Modifier.align(Alignment.Center),
                )

                uiState.messages.isEmpty() && uiState.endReached -> Text(
                    text = stringResource(R.string.no_messages),
                    modifier = Modifier.align(Alignment.Center),
                )

                else -> LazyColumn(
                    state = listState,
                    reverseLayout = true,
                    modifier = Modifier.fillMaxSize(),
                ) {
                    items(uiState.messages, key = Message::id) { message ->
                        MessageItem(
                            message = message,
                            imageUrls = imageUrls,
                            onImageClick = onImageClick,
                        )
                    }
                    if (uiState.isLoading) {
                        item {
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator()
                            }
                        }
                    }
                    if (uiState.isError) {
                        item {
                            ErrorBlock(onRetry = onLoadMore, modifier = Modifier.fillMaxWidth())
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadMoreEffect(
    listState: LazyListState,
    itemsCount: Int,
    onLoadMore: () -> Unit,
) {
    val shouldLoadMore by remember(listState) {
        derivedStateOf {
            val layoutInfo = listState.layoutInfo
            val lastVisible = layoutInfo.visibleItemsInfo.lastOrNull()?.index ?: return@derivedStateOf false
            lastVisible >= layoutInfo.totalItemsCount - LOAD_MORE_THRESHOLD
        }
    }
    LaunchedEffect(shouldLoadMore, itemsCount) {
        if (shouldLoadMore) onLoadMore()
    }
}

@Composable
private fun MessageItem(
    message: Message,
    imageUrls: ImageUrls,
    onImageClick: (String) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(text = message.from, style = MaterialTheme.typography.labelMedium)
        val text = message.data.text
        if (text != null) {
            Text(text = text.text)
        }
        val image = message.data.image
        if (image != null) {
            AsyncImage(
                model = imageUrls.thumbnail(image.link),
                contentDescription = stringResource(R.string.image),
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .size(160.dp)
                    .clickable { onImageClick(image.link) },
            )
        }
    }
}

@Composable
private fun ErrorBlock(
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(text = stringResource(R.string.messages_load_error))
        Button(onClick = onRetry) {
            Text(text = stringResource(R.string.retry))
        }
    }
}
