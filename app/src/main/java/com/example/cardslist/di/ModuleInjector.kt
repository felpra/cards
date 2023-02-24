package com.example.cardslist.di

import android.content.Context
import cardslist.BuildConfig.API_HOST
import cardslist.BuildConfig.API_KEY
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.cardslist.data.repositories.api.KeyInterceptor
import com.example.cardslist.data.room.LocalRoom
import com.example.cardslist.data.room.CardDao
import com.squareup.moshi.Moshi
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class ModuleInjector {

    companion object {
        val baseUrl = "https://omgvamp-hearthstone-v1.p.rapidapi.com"
    }

    @Provides
    fun provideHTTPLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return interceptor;
    }

    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(KeyInterceptor(API_KEY, API_HOST))
            .build()
    }

    @Provides
    fun moshi(): Moshi {
        return Moshi.Builder()
            .build()
    }


    @Provides
    fun provideRetrofit(okHttpClient: OkHttpClient, moshi: Moshi): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .client(okHttpClient)
            .build()
    }


    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): LocalRoom {
        return LocalRoom.getInstance(appContext)
    }

    @Provides
    fun provideCardDao(appDatabase: LocalRoom): CardDao {
        return appDatabase.cardDao()
    }
}