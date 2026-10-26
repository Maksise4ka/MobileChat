package com.github.maksise4ka.chats

import android.app.Application
import com.github.maksise4ka.chats.di.networkModule
import com.github.maksise4ka.chats.di.viewModelModule
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.android.x.androidXModule

class ChatsApplication : Application(), DIAware {
    override val di by DI.lazy {
        import(androidXModule(this@ChatsApplication))
        import(networkModule)
        import(viewModelModule)
    }
}
