package com.test.novel

import android.app.Application
import com.test.novel.utils.AppUtils
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App : Application(){
    override fun onCreate() {
        super.onCreate()
        AppUtils.context = this
    }
}