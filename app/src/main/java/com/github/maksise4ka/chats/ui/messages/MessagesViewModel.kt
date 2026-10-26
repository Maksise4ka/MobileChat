package com.github.maksise4ka.chats.ui.messages

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.maksise4ka.chats.data.remote.model.Message
import com.github.maksise4ka.chats.data.repository.ChatRepository
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

class MessagesViewModel(
    private val channel: String,
    private val repository: ChatRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(MessagesUiState())
    val uiState: StateFlow<MessagesUiState> = _uiState.asStateFlow()

    init {
        loadMore()
    }

    fun loadMore() {
        val state = _uiState.value
        if (state.isLoading || state.endReached) return

        _uiState.update { it.copy(isLoading = true, isError = false) }
        viewModelScope.launch {
            try {
                val page = repository.getMessagesBefore(channel, state.messages.lastOrNull()?.id)
                _uiState.update {
                    it.copy(
                        messages = (it.messages + page).distinctBy(Message::id).toImmutableList(),
                        isLoading = false,
                        endReached = page.isEmpty(),
                    )
                }
            } catch (_: IOException) {
                onError()
            } catch (_: HttpException) {
                onError()
            } catch (_: SerializationException) {
                onError()
            }
        }
    }

    private fun onError() {
        _uiState.update { it.copy(isLoading = false, isError = true) }
    }
}
