package com.medinet

import android.app.Application
import com.medinet.servicelocator.SL

class MedinetApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        SL.init(this)
    }
}