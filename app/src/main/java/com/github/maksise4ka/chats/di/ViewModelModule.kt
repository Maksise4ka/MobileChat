package com.github.maksise4ka.chats.di

import com.github.maksise4ka.chats.ui.chats.ChatsViewModel
import com.github.maksise4ka.chats.ui.messages.MessagesViewModel
import org.kodein.di.DI
import org.kodein.di.bindFactory
import org.kodein.di.bindProvider
import org.kodein.di.instance

val viewModelModule = DI.Module("viewModel") {
    bindProvider { ChatsViewModel(instance()) }
    bindFactory { channel: String -> MessagesViewModel(channel, instance()) }
}
