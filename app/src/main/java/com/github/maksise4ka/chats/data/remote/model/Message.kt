package com.github.maksise4ka.chats.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Message(
    val id: Long,
    val from: String,
    val to: String,
    val data: MessageData,
    val time: Long,
)

@Serializable
data class MessageData(
    @SerialName("Text") val text: TextData? = null,
    @SerialName("Image") val image: ImageData? = null,
)

@Serializable
data class TextData(val text: String)

@Serializable
data class ImageData(val link: String)
