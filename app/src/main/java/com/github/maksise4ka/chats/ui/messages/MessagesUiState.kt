package com.github.maksise4ka.chats.ui.messages

import com.github.maksise4ka.chats.data.remote.model.Message
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf

data class MessagesUiState(
    val messages: ImmutableList<Message> = persistentListOf(),
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val endReached: Boolean = false,
)
