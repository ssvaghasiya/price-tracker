package com.example.transoapp

import android.app.Application
import com.example.transoapp.di.ApplicationComponent
import com.example.transoapp.di.DaggerApplicationComponent

class MyApp : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.factory().create(this)
    }
}