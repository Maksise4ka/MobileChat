package com.github.maksise4ka.chats.ui.chats

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.maksise4ka.chats.data.repository.ChatRepository
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

class ChatsViewModel(private val repository: ChatRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<ChatsUiState>(ChatsUiState.Loading)
    val uiState: StateFlow<ChatsUiState> = _uiState.asStateFlow()

    init {
        loadChannels()
    }

    fun loadChannels() {
        viewModelScope.launch {
            _uiState.value = ChatsUiState.Loading
            _uiState.value = try {
                ChatsUiState.Content(repository.getChannels().toImmutableList())
            } catch (_: IOException) {
                ChatsUiState.Error
            } catch (_: HttpException) {
                ChatsUiState.Error
            } catch (_: SerializationException) {
                ChatsUiState.Error
            }
        }
    }
}
