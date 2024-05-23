package com.hyperether.getgoing_kmp.android

import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import com.hyperether.getgoing_kmp.di.AppContainer
import com.hyperether.getgoing_kmp.di.Factory
import com.hyperether.getgoing_kmp.repository.GgRepository

class App : Application() {
    lateinit var appContainer: AppContainer

    init {
        instance = this
        appContainer = AppContainer(Factory(this))
    }

    companion object {
        private var instance: App? = null
        private var handler: Handler? = null

        fun appCtxt(): Context {
            return instance!!.applicationContext
        }

        fun getRepository(): GgRepository {
            return instance!!.appContainer.appRepository
        }

        fun getHandler(): Handler {
            if (handler == null) {
                val thread = HandlerThread("ggthread")
                thread.start()
                handler = android.os.Handler(thread.looper)
            }
            return handler as Handler
        }
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}