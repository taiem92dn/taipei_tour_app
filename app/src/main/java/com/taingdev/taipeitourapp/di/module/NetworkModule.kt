package com.taingdev.taipeitourapp.di.module

import android.app.Application
import com.taingdev.taipeitourapp.data.AttractionDataSource
import com.taingdev.taipeitourapp.data.RemoteAttractionDataSource
import com.taingdev.taipeitourapp.network.AttractionService
import com.taingdev.taipeitourapp.util.Utils
import com.taingdev.taipeitourapp.network.INetworkCheckService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @Singleton
    fun provideAttractionDataSource(attractionService: AttractionService): AttractionDataSource {
        return RemoteAttractionDataSource(attractionService)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        val addQueryTokenInterceptor = Interceptor { chain ->
            val original = chain.request()
            val url = original.url.newBuilder()
//                .addQueryParameter("appid", apiKey)
                .build()
            val request = original.newBuilder().url(url).build()

            chain.proceed(request)
        }
        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
//            .baseUrl("https://www.travel.taipei/open-api/{lang}/")
            .baseUrl("https://www.travel.taipei/open-api/en/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideAttractionService(retrofit: Retrofit): AttractionService {
        return retrofit.create(AttractionService::class.java)
    }

    @Provides
    @Singleton
    fun provideNetworkCheckService(application: Application): INetworkCheckService {
        return object : INetworkCheckService {
            override fun hasInternet(): Boolean {
                return Utils.hasInternet(application)
            }
        }
    }
}