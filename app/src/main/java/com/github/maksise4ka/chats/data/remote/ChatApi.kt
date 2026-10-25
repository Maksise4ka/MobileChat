package com.github.maksise4ka.chats.data.remote

import com.github.maksise4ka.chats.data.remote.model.Message
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ChatApi {
    @GET("channels")
    suspend fun getChannels(): List<String>

    @GET("channel/{channel}")
    suspend fun getChannelMessages(
        @Path("channel") channel: String,
        @Query("limit") limit: Int,
        @Query("lastKnownId") lastKnownId: Long,
        @Query("reverse") reverse: Boolean,
    ): List<Message>
}
