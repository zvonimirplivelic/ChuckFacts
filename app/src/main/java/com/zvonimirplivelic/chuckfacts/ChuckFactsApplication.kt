package com.zvonimirplivelic.chuckfacts

import android.app.Application
import timber.log.Timber

class ChuckFactsApplication: Application() {
    override fun onCreate() {
        super.onCreate()

        Timber.plant(Timber.DebugTree())
    }
}