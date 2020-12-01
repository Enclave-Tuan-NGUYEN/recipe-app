package com.project.recipedemo.application

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.project.recipedemo.database.MainDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    @Provides
    @Singleton
    internal fun provideContext(application: Application): Context {
        return application.applicationContext
    }

    @Singleton
    @Provides
    fun provideMainDatabase(context: Context): MainDatabase {
        return Room.databaseBuilder(context, MainDatabase::class.java, "AppDB")
            .createFromAsset("database.db")
            .build()
    }
}
