package com.example.assessmenttest.application

import android.app.Application
import com.example.assessmenttest.ki.*

import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level


class App : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@App)
            modules(

                commentsAdaptorModule,
                adaptorModule,
                repositoryModule,
                dbmodule,
                viewModelModule,
                testingviewModelModule,
                favPostrepositoryModule,
                favPostadaptorModule,
                favPostviewModelModule,
                commentsModelModule,
                retrofitModule,
                apiModule

            )
        }
    }
}