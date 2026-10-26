package com.github.maksise4ka.chats.data.remote

class ImageUrls(private val baseUrl: String) {
    fun thumbnail(link: String): String = "${baseUrl}thumb/$link"

    fun full(link: String): String = "${baseUrl}img/$link"
}
