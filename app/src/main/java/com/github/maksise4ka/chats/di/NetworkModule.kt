package com.github.maksise4ka.chats.di

import com.github.maksise4ka.chats.BuildConfig
import com.github.maksise4ka.chats.data.remote.ChatApi
import com.github.maksise4ka.chats.data.remote.ImageUrls
import com.github.maksise4ka.chats.data.repository.ChatRepository
import com.github.maksise4ka.chats.data.repository.NetworkChatRepository
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.create

val networkModule = DI.Module("network") {
    val baseUrl = "https://faerytea.name/"

    bindSingleton {
        Json { ignoreUnknownKeys = true }
    }

    bindSingleton {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor().apply {
                    level = if (BuildConfig.DEBUG) {
                        HttpLoggingInterceptor.Level.BASIC
                    } else {
                        HttpLoggingInterceptor.Level.NONE
                    }
                }
            )
            .build()
    }

    bindSingleton {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(instance())
            .addConverterFactory(instance<Json>().asConverterFactory("application/json".toMediaType()))
            .build()
    }

    bindSingleton { ImageUrls(baseUrl) }

    bindSingleton<ChatApi> { instance<Retrofit>().create() }
    bindSingleton<ChatRepository> { NetworkChatRepository(instance()) }
}
