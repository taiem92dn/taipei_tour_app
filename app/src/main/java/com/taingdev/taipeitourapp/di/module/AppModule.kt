package com.taingdev.taipeitourapp.di.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import com.taingdev.taipeitourapp.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Named
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    @Named("ioDispatcher")
    fun provideIoDispatcher() = Dispatchers.IO

    @Provides
    @Singleton
    fun provideSharedPreferences(context: Application): SharedPreferences =
        context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
}