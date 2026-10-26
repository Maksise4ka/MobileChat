package com.github.maksise4ka.chats.ui.chats

import kotlinx.collections.immutable.ImmutableList

sealed interface ChatsUiState {
    data object Loading : ChatsUiState

    data object Error : ChatsUiState

    data class Content(val channels: ImmutableList<String>) : ChatsUiState
}
