package com.github.maksise4ka.chats.data.repository

import com.github.maksise4ka.chats.data.remote.ChatApi
import com.github.maksise4ka.chats.data.remote.model.Message

class NetworkChatRepository(private val api: ChatApi) : ChatRepository {
    override suspend fun getChannels(): List<String> = api.getChannels()

    override suspend fun getMessagesBefore(channel: String, beforeId: Long?): List<Message> =
        api.getChannelMessages(
            channel = channel,
            limit = PAGE_SIZE,
            lastKnownId = beforeId ?: Long.MAX_VALUE,
            reverse = true,
        )

    companion object {
        const val PAGE_SIZE = 20
    }
}
