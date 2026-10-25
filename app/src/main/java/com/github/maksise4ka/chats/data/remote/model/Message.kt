package com.github.maksise4ka.chats.data.remote.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerializationException
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

@Serializable
data class Message(
    val id: Long,
    val from: String,
    val to: String,
    val data: MessageContent,
    val time: Long,
)

@Serializable(with = MessageContentSerializer::class)
sealed interface MessageContent {
    data class Text(val text: String) : MessageContent
    data class Image(val link: String) : MessageContent
}

@Serializable
private data class MessageContentSurrogate(
    @SerialName("Text") val text: TextSurrogate? = null,
    @SerialName("Image") val image: ImageSurrogate? = null,
)

@Serializable
private data class TextSurrogate(val text: String)

@Serializable
private data class ImageSurrogate(val link: String)

object MessageContentSerializer : KSerializer<MessageContent> {
    override val descriptor: SerialDescriptor = MessageContentSurrogate.serializer().descriptor

    override fun serialize(encoder: Encoder, value: MessageContent) {
        val surrogate = when (value) {
            is MessageContent.Text -> MessageContentSurrogate(text = TextSurrogate(value.text))
            is MessageContent.Image -> MessageContentSurrogate(image = ImageSurrogate(value.link))
        }
        encoder.encodeSerializableValue(MessageContentSurrogate.serializer(), surrogate)
    }

    override fun deserialize(decoder: Decoder): MessageContent {
        val surrogate = decoder.decodeSerializableValue(MessageContentSurrogate.serializer())
        return when {
            surrogate.text != null -> MessageContent.Text(surrogate.text.text)
            surrogate.image != null -> MessageContent.Image(surrogate.image.link)
            else -> throw SerializationException("Message data must contain Text or Image")
        }
    }
}
