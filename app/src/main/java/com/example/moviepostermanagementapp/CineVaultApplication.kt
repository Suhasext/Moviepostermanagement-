package com.example.moviepostermanagementapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CineVaultApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}
