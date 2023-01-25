package com.vholodynskyi.assignment

import android.app.Application
import com.vholodynskyi.assignment.data.datasource.di.GlobalFactory

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        GlobalFactory.init(this)
    }
}