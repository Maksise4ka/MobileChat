package com.github.maksise4ka.chats.data.repository

import com.github.maksise4ka.chats.data.remote.model.Message

interface ChatRepository {
    suspend fun getChannels(): List<String>

    suspend fun getMessagesBefore(channel: String, beforeId: Long?): List<Message>
}
